package chetrari.vlad.rts.data.repository

import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.model.Species_
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesRepository @Inject constructor(box: Box<Species>) : ObjectBoxRepository<Species>(box, Species_.id)
