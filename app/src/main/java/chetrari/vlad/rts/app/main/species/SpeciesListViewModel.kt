package chetrari.vlad.rts.app.main.species

import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.network.fetch.SpeciesByCountryDataUpdater
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.model.Country_
import chetrari.vlad.rts.data.persistence.model.Species_
import chetrari.vlad.rts.data.repository.ObjectBoxRepository
import chetrari.vlad.rts.data.repository.SpeciesRepository
import javax.inject.Inject

class SpeciesListViewModel @Inject constructor(
    private val repository: SpeciesRepository,
    private val byCountryDataUpdater: SpeciesByCountryDataUpdater
) : BaseViewModel() {

    fun speciesByCountry(country: Country) = repository.pagedLiveDataByQuery(
        updateProperties = ObjectBoxRepository.UpdateProperties(
            scope = viewModelScope,
            procedure = { byCountryDataUpdater(country) }),
        config = pagedListConfig,
        query = {
            order(Species_.scientificName)
                .link(Species_.countries)
                .equal(Country_.isoCode, country.isoCode)
        })
        .also { it.updatable.register() }

    fun onRefresh() = updateRegistered()

    private companion object {
        val pagedListConfig: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(20)
            .setPrefetchDistance(40)
            .build()
    }
}