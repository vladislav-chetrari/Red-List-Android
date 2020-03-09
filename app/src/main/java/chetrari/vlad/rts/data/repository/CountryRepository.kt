package chetrari.vlad.rts.data.repository

import androidx.lifecycle.LiveData
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.base.EventMediatorLiveData
import chetrari.vlad.rts.data.model.ui.Country
import chetrari.vlad.rts.data.network.fetch.CountryListFetcher
import io.objectbox.Box
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class CountryRepository @Inject constructor(
    private val box: Box<Country>,
    private val fetcher: CountryListFetcher
) : ObjectBoxRepository<Country>(box) {

    fun get(
        coroutineScope: CoroutineScope,
        query: QueryBuilder<Country>.() -> Unit
    ): LiveData<Event<List<Country>>> = EventMediatorLiveData<List<Country>>(updater = {
        addFetcherSource(fetcher.invoke(coroutineScope, Unit))
    }).also {
        it.addDbQuerySource(query)
        if (box.isEmpty) it.refresh()
    }
}