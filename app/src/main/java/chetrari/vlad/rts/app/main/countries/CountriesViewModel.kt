package chetrari.vlad.rts.app.main.countries

import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.base.invoke
import chetrari.vlad.rts.data.domain.CountryListFetcher
import chetrari.vlad.rts.data.domain.SpeciesByCountryFetcher
import chetrari.vlad.rts.data.network.model.Country
import javax.inject.Inject

class CountriesViewModel @Inject constructor(
    countryListFetcher: CountryListFetcher,
    private val speciesByCountryFetcher: SpeciesByCountryFetcher
) : BaseViewModel() {

    val countries = countryListFetcher(viewModelScope)
    val countryInProgress = actionLiveData<Country?>()
    val speciesByCountry = actionLiveData(speciesByCountryFetcher.liveData)

    fun onCountrySelected(country: Country) {
        countryInProgress.mutable.postValue(country)
        speciesByCountryFetcher(viewModelScope, country)
    }
}