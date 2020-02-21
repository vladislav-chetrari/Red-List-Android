package chetrari.vlad.rts.base

sealed class Event<T> {
    //TODO make progress object
    class Progress<T> : Event<T>()
    //
    class Error<T>(val error: Throwable) : Event<T>()
    class Success<T>(val result: T) : Event<T>()
}