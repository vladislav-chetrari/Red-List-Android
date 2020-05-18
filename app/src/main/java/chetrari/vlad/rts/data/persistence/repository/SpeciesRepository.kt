package chetrari.vlad.rts.data.persistence.repository

import androidx.paging.PagedList
import chetrari.vlad.rts.data.network.update.*
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.model.Country_
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.model.Species_
import chetrari.vlad.rts.data.persistence.type.Vulnerability
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.query.QueryBuilder.StringOrder.CASE_INSENSITIVE
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

    override val orderBy: Property<Species> = Species_.scientificName

    fun byCountryPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<Species>> = UpdateIf.Empty,
        country: Country
    ) = byQueryPaged(context, config, updateIf, Query(countries = listOf(country)))

    fun byVulnerabilityPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<Species>> = UpdateIf.Empty,
        vulnerability: Vulnerability
    ) = byQueryPaged(context, config, updateIf, Query(vulnerability = listOf(vulnerability)))

    fun byCountryAndVulnerabilityPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<Species>> = UpdateIf.Empty,
        country: Country,
        vulnerability: Vulnerability
    ) = byQueryPaged(context, config, updateIf, Query(countries = listOf(country), vulnerability = listOf(vulnerability)))

    fun byQueryPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<Species>> = UpdateIf.Empty,
        query: Query
    ) = byParamsPaged(
        context, config, updateIf,
        byName(query.speciesName),
        byCountries(query.countries),
        byVulnerability(query.vulnerability)
    )

    override suspend fun updateById(id: Long) {
        detailsByIdDataUpdater(id)
        imageDataUpdater(id)
        narrativeByIdDataUpdater(id)
    }

    private fun byCountries(countries: List<Country>) = Param(countries, {
        countries.forEach { byCountryDataUpdater(it) }
    }) {
        if (countries.isNotEmpty()) link(Species_.countries).`in`(Country_.isoCode, countries.map { it.isoCode }.toTypedArray())
        this
    }

    private fun byVulnerability(vulnerability: List<Vulnerability>) = Param(vulnerability, {
        vulnerability.forEach { byVulnerabilityDataUpdater(it) }
    }) {
        if (vulnerability.isEmpty()) this
        else `in`(Species_.category, vulnerability.map { "$it" }.toTypedArray())
    }

    private fun byName(name: String) = Param(name) {
        if (name.isNotBlank()) contains(Species_.scientificName, name, CASE_INSENSITIVE).or()
            .contains(Species_.commonName, name, CASE_INSENSITIVE)
        else this
    }

    data class Query(
        val speciesName: String = "",
        val countries: List<Country> = emptyList(),
        val vulnerability: List<Vulnerability> = emptyList()
    )
}