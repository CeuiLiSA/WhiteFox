package ceui.lisa.hermes.loadstate

sealed class LoadReason {

    object InitialLoad : LoadReason()
    object Preload : LoadReason()
    object PullRefresh : LoadReason()
    object LoadMore : LoadReason()
    object ErrorRetry : LoadReason()
    object EmptyRetry : LoadReason()
}