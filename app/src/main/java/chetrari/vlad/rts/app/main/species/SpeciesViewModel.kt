package chetrari.vlad.rts.app.main.species

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.data.domain.SpeciesByCountryFetcher
import chetrari.vlad.rts.data.network.model.Country
import chetrari.vlad.rts.data.network.model.Species
import javax.inject.Inject

class SpeciesViewModel @Inject constructor(
    private val speciesByCountryFetcher: SpeciesByCountryFetcher
) : BaseViewModel() {

    val species: LiveData<Event<List<Species>>> = MediatorLiveData<Event<List<Species>>>().apply {
        addSource(speciesByCountryFetcher.liveData, ::postValue)
        //maybe another sources
    }

    fun onSearchByCountry(country: Country) {
        speciesByCountryFetcher(viewModelScope, country)
    }
}