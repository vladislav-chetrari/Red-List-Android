package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class EventMediatorLiveData<T>(source: LiveData<T>? = null) : MediatorLiveData<Event<T>>() {

    init {
        if (source != null) addSource(source)
    }

    fun addSource(source: LiveData<T>) {
        addSource(source) { postValue(Event.Success(it)) }
    }
}