package chetrari.vlad.rts.data.network.fetch

import chetrari.vlad.rts.data.network.api.RedListApi
import chetrari.vlad.rts.data.network.model.SpeciesResponse
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.type.Vulnerability
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesByCountryDataUpdater @Inject constructor(
    private val api: RedListApi,
    private val box: Box<Species>
) : DataUpdater<Country, List<SpeciesResponse>, List<Species>>() {

    override suspend fun fetch(input: Country) = api.speciesByCountry(input.isoCode).body.result

    override suspend fun map(input: Country, response: List<SpeciesResponse>) = response
        .map { Species(id = it.taxonId, scientificName = it.scientificName, category = it.category ?: Vulnerability.NE) }

    override suspend fun persist(input: Country, output: List<Species>) {
        val entities = output
            .map { (box[it.id] ?: Species()) to it }
            .map {
                it.first.id = it.second.id
                it.first.category = it.second.category
                it.first.scientificName = it.second.scientificName
                it.first
            }
        entities.forEach {
            box.attach(it)
            it.countries.add(input)
        }
        box.put(entities)
    }
}