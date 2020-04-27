package chetrari.vlad.rts.data.repository

import chetrari.vlad.rts.data.network.fetch.CountryListDataUpdater
import chetrari.vlad.rts.data.network.fetch.invoke
import chetrari.vlad.rts.data.persistence.model.Country
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryLiveRepository @Inject constructor(
    box: Box<Country>,
    private val countryDataUpdater: CountryListDataUpdater
) : LiveRepository<Country>(box) {

    override val all: List<Country>
        get() = super.all.sortedBy(Country::name)

    override suspend fun updateAll() = countryDataUpdater()
}