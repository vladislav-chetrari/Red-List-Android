package chetrari.vlad.rts.app.main.species.details

import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.network.fetch.SpeciesDetailsByIdDataUpdater
import chetrari.vlad.rts.data.network.fetch.SpeciesImageDataUpdater
import chetrari.vlad.rts.data.network.fetch.SpeciesNarrativeByIdDataUpdater
import chetrari.vlad.rts.data.repository.ObjectBoxRepository
import chetrari.vlad.rts.data.repository.ObjectBoxRepository.UpdateProperties.Mode.OnCondition
import chetrari.vlad.rts.data.repository.SpeciesRepository
import javax.inject.Inject

class SpeciesViewModel @Inject constructor(
    private val repository: SpeciesRepository,
    private val detailsByIdDataUpdater: SpeciesDetailsByIdDataUpdater,
    private val narrativeByIdDataUpdater: SpeciesNarrativeByIdDataUpdater,
    private val imageDataUpdater: SpeciesImageDataUpdater
) : BaseViewModel() {

    fun byId(speciesId: Long) = repository.liveByID(
        speciesId, ObjectBoxRepository.UpdateProperties(
            mode = OnCondition(repository[speciesId]?.commonName?.isBlank()),
            scope = viewModelScope,
            procedure = {
                detailsByIdDataUpdater(speciesId)
                imageDataUpdater(speciesId)
                narrativeByIdDataUpdater(speciesId)
            }
        )).also { it.updatable.register() }

    fun onRefresh() = updateRegistered()
}
