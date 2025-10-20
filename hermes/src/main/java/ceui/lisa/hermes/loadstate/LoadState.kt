package ceui.lisa.hermes.loadstate

sealed class LoadState<out T> {
    data class Loading(val reason: LoadReason) : LoadState<Nothing>()
    data class Processing(val progress: Float) : LoadState<Nothing>()
    data class Loaded<out T>(val data: T) : LoadState<T>()
    data class Error(val ex: Exception) : LoadState<Nothing>()
}
