package chetrari.vlad.rts.data.repository

import androidx.lifecycle.LiveData
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.base.EventMediatorLiveData
import chetrari.vlad.rts.data.network.fetch.Fetcher
import io.objectbox.Box
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder

open class ObjectBoxRepository<T>(private val box: Box<T>) {

    protected fun EventMediatorLiveData<List<T>>.addDbQuerySource(
        query: QueryBuilder<T>.() -> Unit
    ) = addSource(ObjectBoxLiveData(box.query { query() })) { postValue(Event.Success(it)) }

    protected fun EventMediatorLiveData<List<T>>.addFetcherSource(
        source: LiveData<Fetcher.Event>
    ) = addSource(source) { fetcherEvent ->
        when (fetcherEvent) {
            Fetcher.Event.Progress -> postValue(Event.Progress)
            is Fetcher.Event.Complete -> {
                fetcherEvent.error?.let { postValue(Event.Error(it)) }
                removeSource(source)
            }
        }
    }
}