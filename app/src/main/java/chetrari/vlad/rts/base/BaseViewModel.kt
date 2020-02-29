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

    protected fun <T> mutableLiveData(initialValue: T? = null): LiveData<T> = MutableLiveData<T>().apply {
        if (initialValue != null) value = initialValue
    }

    protected fun <T> MediatorLiveData<Event<T>>.addSource(source: LiveData<Event<T>>) = addSource(source) {
        postValue(it)
        if (it is Event.Success || it is Event.Error) removeSource(source)
    }

    protected fun <T> eventMediatorLiveData(
        vararg sources: LiveData<Event<T>> = emptyArray()
    ): LiveData<Event<T>> = MediatorLiveData<Event<T>>().apply {
        sources.forEach { addSource(it) }
    }
}