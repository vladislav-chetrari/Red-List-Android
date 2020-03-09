package chetrari.vlad.rts.app.main.countries

import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.model.ui.Country_
import chetrari.vlad.rts.data.repository.CountryRepository
import javax.inject.Inject

class CountriesViewModel @Inject constructor(repository: CountryRepository) : BaseViewModel() {

    val countries = repository.get(viewModelScope) { order(Country_.name) }

    fun onRefresh() = countries.eventMediator.refresh()
}