package ceui.lisa.hermes.loadstate

import kotlinx.coroutines.flow.StateFlow

interface RefreshOwner {
    fun refresh(reason: LoadReason)
    val loadState: StateFlow<LoadState>
}