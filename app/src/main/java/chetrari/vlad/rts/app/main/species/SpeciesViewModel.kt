package chetrari.vlad.rts.app.main.species

import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.data.domain.SpeciesByCountryFetcher
import chetrari.vlad.rts.data.domain.SpeciesImageLinkUpdater
import chetrari.vlad.rts.data.network.model.Country
import chetrari.vlad.rts.data.network.model.Species
import javax.inject.Inject

class SpeciesViewModel @Inject constructor(
    private val speciesByCountryFetcher: SpeciesByCountryFetcher,
    private val speciesImageLinkUpdater: SpeciesImageLinkUpdater
) : BaseViewModel() {

    val species = mediatorLiveData<Event<List<Species>>>()

    fun onSearchByCountry(country: Country) = species.mediator.addSource(speciesByCountryFetcher(viewModelScope, country))

    fun onLoadSpeciesImages(species: Species) = speciesImageLinkUpdater(viewModelScope, species)
}