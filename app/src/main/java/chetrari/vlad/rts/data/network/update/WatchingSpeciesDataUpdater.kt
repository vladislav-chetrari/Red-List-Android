package chetrari.vlad.rts.data.network.update

import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.model.Species_
import io.objectbox.Box
import io.objectbox.kotlin.query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchingSpeciesDataUpdater @Inject constructor(
    private val box: Box<Species>,
    private val byIdDataUpdater: SpeciesDetailsByIdDataUpdater
) {
    suspend operator fun invoke() = box.query { equal(Species_.watching, true) }.findIds().forEach { byIdDataUpdater(it) }
}