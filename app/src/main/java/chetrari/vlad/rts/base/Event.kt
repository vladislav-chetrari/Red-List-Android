package chetrari.vlad.rts.base

sealed class Event<out T> {
    object Progress : Event<Nothing>()
    class Success<T>(val result: T) : Event<T>()
    class Error<T>(val error: Throwable) : Event<T>()
}