package chetrari.vlad.redlist.data.persistence.repository

import chetrari.vlad.redlist.data.network.update.CountryListDataUpdater
import chetrari.vlad.redlist.data.network.update.invoke
import chetrari.vlad.redlist.data.persistence.model.Country
import chetrari.vlad.redlist.data.persistence.model.Country_
import io.objectbox.Box
import io.objectbox.Property
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepository @Inject constructor(
    box: Box<Country>,
    errorMapper: RepositoryErrorMapper,
    private val countryDataUpdater: CountryListDataUpdater
) : LiveRepository<Country>(box, errorMapper) {

    override val idProperty: Property<Country> = Country_.id
    override val defaultOrderByProperty: Property<Country> = Country_.name

    override suspend fun updateAll() = countryDataUpdater()
}