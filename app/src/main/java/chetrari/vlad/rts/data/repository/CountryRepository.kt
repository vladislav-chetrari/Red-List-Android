package chetrari.vlad.rts.data.repository

import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.model.Country_
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepository @Inject constructor(box: Box<Country>) : ObjectBoxRepository<Country>(box, Country_.id)
