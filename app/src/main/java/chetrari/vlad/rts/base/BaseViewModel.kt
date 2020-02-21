package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    protected val <T> LiveData<Event<T>>.mutable: MutableLiveData<Event<T>>
        get() = this as MutableLiveData

    protected fun <T> liveData(initialValue: T? = null): LiveData<Event<T>> =
        if (initialValue == null) MutableLiveData<Event<T>>()
        else MutableLiveData(Event.Success(initialValue))

    protected fun <T> launchOnIO(
        mutableLiveData: MutableLiveData<Event<T>>,
        block: suspend () -> T
    ) = launchBackgroundTask(Dispatchers.IO, mutableLiveData, block)

    protected fun <T> launchComputation(
        mutableLiveData: MutableLiveData<Event<T>>,
        block: suspend () -> T
    ) = launchBackgroundTask(Dispatchers.Default, mutableLiveData, block)

    private fun <T> launchBackgroundTask(
        dispatcher: CoroutineDispatcher,
        mutableLiveData: MutableLiveData<Event<T>>,
        block: suspend () -> T
    ) {
        mutableLiveData.postValue(Event.Progress())
        viewModelScope.launch {
            try {
                val result = withContext(dispatcher) { block() }
                mutableLiveData.postValue(Event.Success(result))
            } catch (t: Throwable) {
                mutableLiveData.postValue(Event.Error(t))
            }
        }
    }
}