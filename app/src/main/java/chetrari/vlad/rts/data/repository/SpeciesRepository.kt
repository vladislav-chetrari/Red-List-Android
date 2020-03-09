package chetrari.vlad.rts.data.repository

import androidx.lifecycle.LiveData
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.base.EventMediatorLiveData
import chetrari.vlad.rts.data.model.ui.Country
import chetrari.vlad.rts.data.model.ui.Country_
import chetrari.vlad.rts.data.model.ui.Species
import chetrari.vlad.rts.data.model.ui.Species_
import chetrari.vlad.rts.data.network.fetch.SpeciesByCountryFetcher
import io.objectbox.Box
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesRepository @Inject constructor(
    private val box: Box<Species>,
    private val speciesByCountryFetcher: SpeciesByCountryFetcher
) : ObjectBoxRepository<Species>(box) {

    fun byCountry(coroutineScope: CoroutineScope, country: Country): LiveData<Event<List<Species>>> {
        return EventMediatorLiveData<List<Species>>(updater = {
            addFetcherSource(speciesByCountryFetcher(coroutineScope, country))
        }).also {
            val queryBuilder: QueryBuilder<Species>.() -> Unit = {
                order(Species_.scientificName)
                    .link(Species_.countries)
                    .equal(Country_.isoCode, country.isoCode)
            }
            it.addDbQuerySource(queryBuilder)
            if (box.query(queryBuilder).count() == 0L) it.refresh()
        }
    }
}