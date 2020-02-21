package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, R> LiveData<Event<T>>.map(consumer: (T) -> R): LiveData<Event<R>> = MediatorLiveData<Event<R>>().apply {
    addSource(this@map) {
        postValue(
            when (it) {
                is Event.Progress -> Event.Progress()
                is Event.Error -> Event.Error(it.error)
                is Event.Success -> Event.Success(consumer(it.result))
            }
        )
    }
}