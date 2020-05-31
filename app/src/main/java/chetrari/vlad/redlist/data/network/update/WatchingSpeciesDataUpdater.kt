package chetrari.vlad.redlist.data.network.update

import chetrari.vlad.redlist.data.persistence.model.Species
import chetrari.vlad.redlist.data.persistence.model.Species_
import io.objectbox.Box
import io.objectbox.kotlin.query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchingSpeciesDataUpdater @Inject constructor(
    private val box: Box<Species>,
    private val detailsByIdDataUpdater: SpeciesDetailsByIdDataUpdater,
//    private val imageDataUpdater: SpeciesImageDataUpdater,
    private val narrativeByIdDataUpdater: SpeciesNarrativeByIdDataUpdater,
    private val speciesWebLinkUpdater: SpeciesWebLinkUpdater
) {
    suspend operator fun invoke() {
        for (id in box.query { equal(Species_.watching, true) }.findIds()) {
            detailsByIdDataUpdater(id)
//FIXME (impacts exec time)            imageDataUpdater(id)
            narrativeByIdDataUpdater(id)
            val scientificName = box[id]?.scientificName ?: continue
            speciesWebLinkUpdater(scientificName)
        }
    }
}