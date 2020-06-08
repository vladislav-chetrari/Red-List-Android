package chetrari.vlad.redlist.data.persistence.repository

import androidx.paging.PagedList
import chetrari.vlad.redlist.data.network.update.*
import chetrari.vlad.redlist.data.persistence.model.Country
import chetrari.vlad.redlist.data.persistence.model.Country_
import chetrari.vlad.redlist.data.persistence.model.Species
import chetrari.vlad.redlist.data.persistence.model.Species_
import chetrari.vlad.redlist.data.persistence.type.Vulnerability
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.kotlin.inValues
import io.objectbox.query.QueryBuilder.StringOrder.CASE_INSENSITIVE
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class SpeciesRepository @Inject constructor(
    private val box: Box<Species>,
    errorMapper: RepositoryErrorMapper,
    private val byCountryDataUpdater: SpeciesByCountryDataUpdater,
    private val detailsByIdDataUpdater: SpeciesDetailsByIdDataUpdater,
    private val narrativeByIdDataUpdater: SpeciesNarrativeByIdDataUpdater,
    private val imageDataUpdater: SpeciesImageDataUpdater,
    private val byVulnerabilityDataUpdater: SpeciesByVulnerabilityDataUpdater,
    private val watchingSpeciesDataUpdater: WatchingSpeciesDataUpdater,
    private val speciesWebLinkUpdater: SpeciesWebLinkUpdater
) : LiveRepository<Species>(box, errorMapper) {

    override val idProperty: Property<Species> = Species_.id
    override val defaultOrderByProperty: Property<Species> = Species_.scientificName

    fun watchingSpeciesPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateOption: UpdateOption<List<Species>>
    ) = byParamsPaged(context, config, updateOption, byWatching())

    fun byCountryPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateOption: UpdateOption<List<Species>> = UpdateOption.OnEmpty,
        country: Country
    ) = byQueryPaged(context, config, updateOption, Query(countries = listOf(country)))

    fun byVulnerabilityPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateOption: UpdateOption<List<Species>> = UpdateOption.OnEmpty,
        vulnerability: Vulnerability
    ) = byQueryPaged(context, config, updateOption, Query(vulnerability = listOf(vulnerability)))

    fun byCountryAndVulnerabilityPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateOption: UpdateOption<List<Species>> = UpdateOption.OnEmpty,
        country: Country,
        vulnerability: Vulnerability
    ) = byQueryPaged(context, config, updateOption, Query(countries = listOf(country), vulnerability = listOf(vulnerability)))

    fun byQueryPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateOption: UpdateOption<List<Species>> = UpdateOption.OnEmpty,
        query: Query
    ) = byParamsPaged(
        context, config, updateOption,
        Param(Unit) { order(defaultOrderByProperty) },
        byCountries(query.countries),
        byVulnerability(query.vulnerability),
        byName(query.speciesName)
    )

    fun toggleWatching(species: Species) {
        species.watching = species.watching.not()
        box.put(species)
    }

    suspend fun updateImagesFromHtml(context: CoroutineContext, speciesId: Long, html: String) = withContext(context) {
        imageDataUpdater(speciesId to html)
    }

    override suspend fun updateById(id: Long) {
        detailsByIdDataUpdater(id)
        narrativeByIdDataUpdater(id)
        val scientificName = box[id]?.scientificName ?: return
        speciesWebLinkUpdater(scientificName)
    }

    private fun byCountries(countries: List<Country>) = Param(countries, {
        countries.forEach { byCountryDataUpdater(it) }
    }) {
        if (countries.isNotEmpty()) link(Species_.countries)
            .inValues(Country_.isoCode, countries.map { it.isoCode }.toTypedArray())
        this
    }

    private fun byVulnerability(vulnerability: List<Vulnerability>) = Param(vulnerability, {
        vulnerability.forEach { byVulnerabilityDataUpdater(it) }
    }) {
        if (vulnerability.isEmpty()) this
        else inValues(Species_.category, vulnerability.map { "$it" }.toTypedArray())
    }

    private fun byName(name: String) = Param(name) {
        if (name.isNotBlank()) contains(Species_.scientificName, name, CASE_INSENSITIVE).or()
            .contains(Species_.commonName, name, CASE_INSENSITIVE)
        else this
    }

    private fun byWatching() = Param(Unit, updater = { watchingSpeciesDataUpdater() }) {
        equal(Species_.watching, true).order(Species_.commonName)
    }

    data class Query(
        val speciesName: String = "",
        val countries: List<Country> = emptyList(),
        val vulnerability: List<Vulnerability> = emptyList()
    )
}