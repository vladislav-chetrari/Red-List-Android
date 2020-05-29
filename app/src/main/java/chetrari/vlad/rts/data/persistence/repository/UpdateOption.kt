package chetrari.vlad.rts.data.persistence.repository

sealed class UpdateOption<out T> {
    object None : UpdateOption<Nothing>()
    object OnEmpty : UpdateOption<Nothing>()
    object Immediate : UpdateOption<Nothing>()
    class Condition<T>(val consumer: (T?) -> Boolean) : UpdateOption<T>()
}