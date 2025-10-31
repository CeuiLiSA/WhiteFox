package ceui.lisa.hermes.loadstate

import kotlinx.coroutines.flow.StateFlow

interface RefreshOwner<ValueT> {
    fun refresh(reason: LoadReason)
    val loadState: StateFlow<LoadState>
    val valueFlow: StateFlow<ValueT?>
}