package chetrari.vlad.rts.base

private class LazyInitializerImpl<out T>(private val initializer: () -> T) : Lazy<T> {
    private var _value: T? = null

    override val value: T
        get() {
            if (_value == null) _value = initializer()
            return _value!!
        }

    override fun isInitialized(): Boolean = _value != null

    override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."
}

fun <T> lazily(initializer: () -> T): Lazy<T> = LazyInitializerImpl(initializer)