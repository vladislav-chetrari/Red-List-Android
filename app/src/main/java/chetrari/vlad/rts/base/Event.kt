package chetrari.vlad.rts.base

sealed class Event<out T> {
    object Progress : Event<Nothing>()
    class Error(val error: Throwable) : Event<Nothing>()
    class Success<T>(val result: T) : Event<T>()
}