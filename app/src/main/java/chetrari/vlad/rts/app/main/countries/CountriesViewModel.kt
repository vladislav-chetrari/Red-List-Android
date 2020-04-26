package chetrari.vlad.rts.app.main.countries

import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.network.fetch.CountryListDataUpdater
import chetrari.vlad.rts.data.network.fetch.invoke
import chetrari.vlad.rts.data.persistence.model.Country_
import chetrari.vlad.rts.data.repository.CountryRepository
import chetrari.vlad.rts.data.repository.ObjectBoxRepository
import javax.inject.Inject


class CountriesViewModel @Inject constructor(
    repository: CountryRepository,
    countryListFetcher: CountryListDataUpdater
) : BaseViewModel() {

    val countries = repository.liveDataByQuery(
        ObjectBoxRepository.UpdateProperties(scope = viewModelScope, procedure = countryListFetcher::invoke)
    ) { order(Country_.name) }

    fun onRefresh() = countries.updatable.update(viewModelScope)
}