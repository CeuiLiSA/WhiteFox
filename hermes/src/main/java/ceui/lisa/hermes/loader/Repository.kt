package ceui.lisa.hermes.loader

import ceui.lisa.hermes.loadstate.LoadReason
import kotlinx.coroutines.flow.StateFlow

interface Repository<ValueT> {
    suspend fun load(reason: LoadReason)

    val valueFlow: StateFlow<ValueT?>
}
