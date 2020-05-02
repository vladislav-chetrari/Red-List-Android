package chetrari.vlad.rts.data.persistence.repository

import androidx.paging.PagedList
import chetrari.vlad.rts.data.network.update.SpeciesByCountryDataUpdater
import chetrari.vlad.rts.data.network.update.SpeciesDetailsByIdDataUpdater
import chetrari.vlad.rts.data.network.update.SpeciesImageDataUpdater
import chetrari.vlad.rts.data.network.update.SpeciesNarrativeByIdDataUpdater
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.model.Country_
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.model.Species_
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class SpeciesRepository @Inject constructor(
    box: Box<Species>,
    private val byCountryDataUpdater: SpeciesByCountryDataUpdater,
    private val detailsByIdDataUpdater: SpeciesDetailsByIdDataUpdater,
    private val narrativeByIdDataUpdater: SpeciesNarrativeByIdDataUpdater,
    private val imageDataUpdater: SpeciesImageDataUpdater
) : LiveRepository<Species>(box) {

    fun byCountryPaged(context: CoroutineContext, config: PagedList.Config, country: Country) = byQueryPagedOrUpdate(
        context = context,
        config = config,
        updateFunction = { byCountryDataUpdater(country) },
        dbQuery = {
            order(Species_.scientificName)
                .link(Species_.countries)
                .equal(Country_.isoCode, country.isoCode)
        })

    override suspend fun updateById(id: Long) {
        detailsByIdDataUpdater(id)
        imageDataUpdater(id)
        narrativeByIdDataUpdater(id)
    }
}