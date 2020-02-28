package chetrari.vlad.rts.app.main.countries

import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.base.invoke
import chetrari.vlad.rts.data.domain.CountryListFetcher
import javax.inject.Inject

class CountriesViewModel @Inject constructor(
    private val countryListFetcher: CountryListFetcher
) : BaseViewModel() {

    val countries = countryListFetcher(viewModelScope)

    fun onRefresh() {
        countryListFetcher(viewModelScope)
    }
}