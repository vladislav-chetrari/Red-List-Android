package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, R> LiveData<Event<T>>.map(consumer: (T) -> R): LiveData<Event<R>> = MediatorLiveData<Event<R>>().apply {
    addSource(this@map) {
        postValue(
            when (it) {
                is Event.Progress -> it
                is Event.Error -> Event.Error(it.error)
                is Event.Success -> Event.Success(consumer(it.result))
            }
        )
    }
}

fun <T> LiveData<Event<T>>.filter(predicate: (T) -> Boolean): LiveData<Event<T>> = MediatorLiveData<Event<T>>().apply {
    addSource(this@filter) {
        if (it is Event.Progress || it is Event.Error || (it is Event.Success && predicate(it.result))) postValue(it)
    }
}