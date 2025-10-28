package ceui.lisa.hermes.loadstate

sealed class LoadState {
    data class Loading(val reason: LoadReason) : LoadState()
    data class Processing(val progress: Float) : LoadState()
    data class Loaded(val hasContent: Boolean) : LoadState()
    data class Error(val ex: Exception) : LoadState()
}
