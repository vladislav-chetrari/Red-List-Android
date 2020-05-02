package chetrari.vlad.rts.data.network.update

import chetrari.vlad.rts.data.network.api.RedListApi
import chetrari.vlad.rts.data.network.model.SpeciesResponse
import chetrari.vlad.rts.data.persistence.model.Species
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SpeciesDetailsByIdDataUpdater @Inject constructor(
    private val api: RedListApi,
    private val box: Box<Species>
) : DataUpdater<Long, SpeciesResponse, Species>() {

    override suspend fun fetch(input: Long): SpeciesResponse {
        val byIdList = api.detailsBySpeciesId(input).body.result
        return when {
            byIdList.isNotEmpty() -> byIdList.first()
            else -> {
                val species = box[input]
                val scientificName = species.scientificName
                val byNameList = api.detailsBySpeciesScientificName(scientificName).body.result
                if (byNameList.isEmpty()) {
                    val commonNames = api.commonNamesByScientificName(scientificName).body.result
                    val first = commonNames.firstOrNull { it.language.equals("eng", true) }
                    SpeciesResponse(commonName = first?.name ?: "")
                } else byNameList.first()
            }
        }
    }

    override suspend fun map(input: Long, response: SpeciesResponse) = Species(
        kingdom = response.kingdom ?: "",
        phylum = response.phylum ?: "",
        bioClass = response.bioClass ?: "",
        family = response.family ?: "",
        genus = response.genus ?: "",
        order = response.order ?: "",
        commonName = response.commonName ?: "",
        populationTrend = response.populationTrend ?: ""
    )

    override suspend fun persist(input: Long, output: Species) {
        val species = box[input] ?: Species()
        species.run {
            kingdom = output.kingdom
            phylum = output.phylum
            bioClass = output.bioClass
            family = output.family
            genus = output.genus
            order = output.order
            commonName = output.commonName
            populationTrend = output.populationTrend
        }
        box.put(species)
    }
}
