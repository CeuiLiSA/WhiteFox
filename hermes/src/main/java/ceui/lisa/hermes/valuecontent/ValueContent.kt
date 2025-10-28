package ceui.lisa.hermes.valuecontent

import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import timber.log.Timber

open class ValueContent<ValueT>(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository<ValueT>,
    private val onDataPrepared: (ValueT) -> Unit,
) : RefreshOwner {

    private val _taskMutex = Mutex()
    protected val _loadStateFlow =
        MutableStateFlow<LoadState>(LoadState.Loading(LoadReason.InitialLoad))
    override val loadState: StateFlow<LoadState> = _loadStateFlow.asStateFlow()
    val valueFlow: StateFlow<ValueT?> = repository.valueFlow

    override fun refresh(reason: LoadReason) {
        coroutineScope.launch {
            withLockSuspend {
                try {
                    _loadStateFlow.value = LoadState.Loading(reason)
                    withContext(Dispatchers.IO) {
                        repository.load(reason)
                    }
                    _loadStateFlow.value = LoadState.Loaded(true)
                } catch (ex: Exception) {
                    Timber.e(ex)
                    if (repository.valueFlow.value == null) {
                        _loadStateFlow.value = LoadState.Error(ex)
                    }
                }
            }
        }
    }

    suspend fun withLockSuspend(
        action: suspend () -> Unit
    ) {
        if (!_taskMutex.tryLock()) return
        try {
            action()
        } finally {
            _taskMutex.unlock()
        }
    }


    init {
        coroutineScope.launch {
            repository.valueFlow.collectLatest { value ->
                if (value != null) {
                    onDataPrepared(value)
                }
            }
        }
        refresh(LoadReason.InitialLoad)
    }
}
