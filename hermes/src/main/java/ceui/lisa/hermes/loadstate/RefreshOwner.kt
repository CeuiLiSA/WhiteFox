package ceui.lisa.hermes.loadstate

interface RefreshOwner {
    fun refresh(reason: LoadReason)
}