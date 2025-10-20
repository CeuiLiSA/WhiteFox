package ceui.lisa.hermes.valuecontent

import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import timber.log.Timber

class ValueContent<ValueT>(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository<ValueT>
) : RefreshOwner {

    private val _taskMutex = Mutex()
    private val _loadStateFlow =
        MutableStateFlow<LoadState<ValueT>>(LoadState.Loading(LoadReason.InitialLoad))
    val loadState: StateFlow<LoadState<ValueT>> = _loadStateFlow.asStateFlow()

    override fun refresh(reason: LoadReason) {
        coroutineScope.launch {
            if (!_taskMutex.tryLock()) return@launch

            _loadStateFlow.value = LoadState.Loading(reason)
            try {
                val data = repository.load(reason)
                _loadStateFlow.value = LoadState.Loaded(data)
            } catch (ex: Exception) {
                Timber.e(ex)
                _loadStateFlow.value = LoadState.Error(ex)
            } finally {
                _taskMutex.unlock()
            }
        }
    }

    init {
        refresh(LoadReason.InitialLoad)
    }
}
