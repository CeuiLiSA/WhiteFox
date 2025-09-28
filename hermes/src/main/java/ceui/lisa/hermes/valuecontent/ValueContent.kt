package ceui.lisa.hermes.valuecontent

import ceui.lisa.hermes.loader.Repository
import ceui.lisa.hermes.loadstate.LoadReason
import ceui.lisa.hermes.loadstate.LoadState
import ceui.lisa.hermes.loadstate.RefreshOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import timber.log.Timber

class ValueContent<ValueT>(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository<ValueT>
) : RefreshOwner {

    private val _taskMutex = Mutex()
    val resultFlow = MutableStateFlow<ValueT?>(null)
    val loadStateFlow = MutableStateFlow<LoadState?>(null)


    override fun refresh(reason: LoadReason) {
        coroutineScope.launch {
            if (!_taskMutex.tryLock()) {
                return@launch
            }

            try {
                loadStateFlow.emit(LoadState.Loading(reason))
                val result = repository.load()
                resultFlow.emit(result)
                loadStateFlow.emit(LoadState.Loaded(true))
            } catch (ex: Exception) {
                Timber.e(ex)
                loadStateFlow.emit(LoadState.Error(ex))
            } finally {
                _taskMutex.unlock()
            }
        }
    }

    init {
        refresh(LoadReason.InitialLoad)
    }
}