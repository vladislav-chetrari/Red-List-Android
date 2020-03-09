package chetrari.vlad.rts.app.main.species

import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.model.ui.Country
import chetrari.vlad.rts.data.model.ui.Species
import chetrari.vlad.rts.data.network.fetch.SpeciesImageLinkUpdater
import chetrari.vlad.rts.data.repository.SpeciesRepository
import javax.inject.Inject

class SpeciesViewModel @Inject constructor(
    private val speciesRepository: SpeciesRepository,
    private val speciesImageLinkUpdater: SpeciesImageLinkUpdater
) : BaseViewModel() {

    fun speciesByCountry(country: Country) = speciesRepository.byCountry(viewModelScope, country)

    fun onUpdateImage(species: Species) {
        speciesImageLinkUpdater(viewModelScope, species)
    }
}