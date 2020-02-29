package chetrari.vlad.rts.base

import androidx.annotation.MainThread
import androidx.lifecycle.*

abstract class BaseViewModel : ViewModel() {

    protected val <T> LiveData<T>.mutable: MutableLiveData<T>
        get() = this as MutableLiveData

    protected val <T> LiveData<T>.mediator: MediatorLiveData<T>
        get() = this as MediatorLiveData

    protected fun <T> mutableLiveData(initialValue: T? = null): LiveData<T> =
        if (initialValue == null) MutableLiveData<T>()
        else MutableLiveData(initialValue)

    protected fun <T> mediatorLiveData(initialValue: T? = null): LiveData<T> =
        if (initialValue == null) MediatorLiveData<T>()
        else MediatorLiveData<T>().apply { value = initialValue }

    protected fun <T> actionLiveData(): LiveData<T> = object : MutableLiveData<T>() {
        @MainThread
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, Observer {
                if (it == null) return@Observer
                observer.onChanged(it)
                value = null
            })
        }
    }

    protected fun <T> actionLiveData(source: LiveData<T>): LiveData<T> = object : MediatorLiveData<T>() {

        init {
            addSource(source, ::postValue)
        }

        @MainThread
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) = super.observe(owner, Observer {
            if (it == null) return@Observer
            observer.onChanged(it)
            value = null
        })
    }

    protected fun <T> MediatorLiveData<Event<T>>.addSource(source: LiveData<Event<T>>) = addSource(source) {
        postValue(it)
        if (it is Event.Success || it is Event.Error) removeSource(source)
    }
}