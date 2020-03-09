package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.objectbox.android.ObjectBoxLiveData

class EventMediatorLiveData<T>(
    source: () -> LiveData<Event<T>>? = { null },
    private val updater: (EventMediatorLiveData<T>.() -> Unit) = {}
) : MediatorLiveData<Event<T>>() {

    init {
        source()?.let(::addSource)
    }

    fun refresh() = updater(this)

    fun addSource(source: LiveData<Event<T>>) = addSource(source) {
        postValue(it)
        if (source !is ObjectBoxLiveData<*> && (it is Event.Success || it is Event.Error)) {
            removeSource(source)
        }
    }
}