package ceui.lisa.hermes.loadstate

sealed class LoadReason {

    object InitialLoad : LoadReason()
    object PullRefresh : LoadReason()
    object ErrorRetry : LoadReason()
    object EmptyRetry : LoadReason()
}