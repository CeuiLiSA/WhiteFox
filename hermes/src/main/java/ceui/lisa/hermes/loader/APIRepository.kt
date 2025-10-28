package ceui.lisa.hermes.loader

import ceui.lisa.hermes.loadstate.LoadReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class APIRepository<ValueT : Any>(
    private val loader: suspend () -> ValueT,
) : Repository<ValueT> {

    private val _valueFlowImpl = MutableStateFlow<ValueT?>(null)
    override val valueFlow: StateFlow<ValueT?> = _valueFlowImpl.asStateFlow()

    override suspend fun load(reason: LoadReason) {
        _valueFlowImpl.value = loader()
    }
}