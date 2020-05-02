package chetrari.vlad.rts.base

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    private val updatableRegistry = arrayListOf<CoroutineUpdatable>()

    protected val context: CoroutineContext
        get() = viewModelScope.coroutineContext

    private val <T> LiveData<T>.mutable: MutableLiveData<T>
        get() = this as MutableLiveData

    protected val <T> LiveData<T>.mediator: MediatorLiveData<T>
        get() = this as MediatorLiveData

    protected val <T> LiveData<T>.updatable: CoroutineUpdatable
        get() = this as CoroutineUpdatable

    protected fun <T> liveData(value: T? = null): LiveData<T> =
        if (value == null) MutableLiveData() else MutableLiveData(value)

    protected fun CoroutineUpdatable.register() {
        updatableRegistry += this
    }

    protected fun <T> MutableLiveData<T>.refresh() = postValue(value)

    protected fun updateRegistered() = updatableRegistry
        .filterIsInstance<EventLiveData<*>>()
        .filter(LiveData<*>::hasActiveObservers)
        .forEach { it.update(viewModelScope) }

    @CallSuper
    override fun onCleared() {
        updatableRegistry.clear()
        super.onCleared()
    }
}