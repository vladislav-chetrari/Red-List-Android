package chetrari.vlad.rts.base

import androidx.lifecycle.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    protected val context: CoroutineContext
        get() = viewModelScope.coroutineContext

    protected val <T> LiveData<T>.mutable: MutableLiveData<T>
        get() = this as MutableLiveData

    protected val <T> LiveData<T>.mediator: MediatorLiveData<T>
        get() = this as MediatorLiveData

    protected fun <T> mutableLiveData(value: T? = null): LiveData<T> =
        if (value == null) MutableLiveData() else MutableLiveData(value)

    protected fun <T> mediatorLiveData(setup: MediatorLiveData<T>.() -> Unit): LiveData<T> = MediatorLiveData<T>().apply(setup)

    protected fun <T> MutableLiveData<T>.refresh() = postValue(value)
}