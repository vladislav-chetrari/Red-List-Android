package chetrari.vlad.rts.data.network.fetch

import chetrari.vlad.rts.data.model.response.SpeciesResponse
import chetrari.vlad.rts.data.model.ui.Country
import chetrari.vlad.rts.data.model.ui.Species
import chetrari.vlad.rts.data.model.ui.Species_
import chetrari.vlad.rts.data.network.api.RedListApi
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesByCountryFetcher @Inject constructor(
    private val api: RedListApi,
    private val box: Box<Species>
) : Fetcher<Country, List<SpeciesResponse>, List<Species>>() {

    override suspend fun fetch(input: Country) = api.speciesByCountry(input.isoCode).body.species

    override suspend fun map(response: List<SpeciesResponse>) = response
        .map { Species(taxonId = it.taxonId, scientificName = it.scientificName, category = it.category) }

    override suspend fun persist(input: Country, output: List<Species>) = box.put(output
        .map { (box.query().equal(Species_.scientificName, it.scientificName).build().findFirst() ?: Species()) to it }
        .map {
            it.first.category = it.second.category
            it.first.taxonId = it.second.taxonId
            it.first.scientificName = it.second.scientificName
            it.first.countries.add(input)
            it.first
        })
}