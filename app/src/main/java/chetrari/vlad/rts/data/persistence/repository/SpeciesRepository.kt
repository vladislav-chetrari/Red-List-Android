package chetrari.vlad.rts.data.persistence.repository

import androidx.paging.PagedList
import chetrari.vlad.rts.data.network.update.*
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.model.Country_
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.model.Species_
import chetrari.vlad.rts.data.persistence.type.Vulnerability
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
    private val imageDataUpdater: SpeciesImageDataUpdater,
    private val byVulnerabilityDataUpdater: SpeciesByVulnerabilityDataUpdater
) : LiveRepository<Species>(box) {

    override val defaultParams: List<Param<*>>
        get() = listOf(QueryParam { order(Species_.scientificName) })

    fun byCountryPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<Species>> = UpdateIf.Empty,
        country: Country
    ) = byParamsPaged(context, config, updateIf, byCountry(country))

    fun byVulnerabilityPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<Species>> = UpdateIf.Empty,
        vulnerability: Vulnerability
    ) = byParamsPaged(context, config, updateIf, byVulnerability(vulnerability))

    fun byCountryAndVulnerabilityPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<Species>> = UpdateIf.Empty,
        country: Country,
        vulnerability: Vulnerability
    ) = byParamsPaged(context, config, updateIf, byCountry(country), byVulnerability(vulnerability))

    override suspend fun updateById(id: Long) {
        detailsByIdDataUpdater(id)
        imageDataUpdater(id)
        narrativeByIdDataUpdater(id)
    }

    private fun byCountry(country: Country) = Param(country, byCountryDataUpdater::invoke, {
        link(Species_.countries).equal(Country_.isoCode, country.isoCode)
    })

    private fun byVulnerability(vulnerability: Vulnerability) = Param(vulnerability, byVulnerabilityDataUpdater::invoke, {
        equal(Species_.category, vulnerability.toString())
    })
}