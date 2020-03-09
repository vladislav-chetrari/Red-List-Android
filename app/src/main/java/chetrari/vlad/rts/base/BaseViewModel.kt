package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected val <T> LiveData<T>.mutable: MutableLiveData<T>
        get() = this as MutableLiveData

    protected val <T> LiveData<T>.mediator: MediatorLiveData<T>
        get() = this as MediatorLiveData

    protected val <T> LiveData<Event<T>>.eventMediator: EventMediatorLiveData<T>
        get() = this as EventMediatorLiveData<T>

    protected fun <T> mutableLiveData(initialValue: T? = null): LiveData<T> = MutableLiveData<T>().apply {
        if (initialValue != null) value = initialValue
    }

    protected fun <T> eventMediatorLiveData(
        source: () -> LiveData<Event<T>>? = { null }
    ): LiveData<Event<T>> = EventMediatorLiveData(source)
}