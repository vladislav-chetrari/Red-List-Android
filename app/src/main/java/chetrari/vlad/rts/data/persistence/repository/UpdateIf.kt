package chetrari.vlad.rts.data.persistence.repository

sealed class UpdateIf<out T> {
    object Empty : UpdateIf<Nothing>()
    object Refresh : UpdateIf<Nothing>()
    class Condition<T>(val consumer: (T?) -> Boolean) : UpdateIf<T>()
}