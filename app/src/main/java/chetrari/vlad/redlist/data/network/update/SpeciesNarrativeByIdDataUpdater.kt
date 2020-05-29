package chetrari.vlad.redlist.data.network.update

import chetrari.vlad.redlist.data.network.api.RedListApi
import chetrari.vlad.redlist.data.network.model.ArrayResponse
import chetrari.vlad.redlist.data.network.model.SpeciesNarrativeResponse
import chetrari.vlad.redlist.data.persistence.model.Narrative
import chetrari.vlad.redlist.data.persistence.model.Species
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesNarrativeByIdDataUpdater @Inject constructor(
    private val api: RedListApi,
    private val box: Box<Species>
) : DataUpdater<Long, ArrayResponse<SpeciesNarrativeResponse>, Narrative>() {

    override suspend fun fetch(input: Long) = api.narrativeBySpeciesId(input).body

    override suspend fun map(input: Long, response: ArrayResponse<SpeciesNarrativeResponse>) = response.result.first().let {
        Narrative(
            taxonomicNotes = it.taxonomicNotes ?: "",
            justification = it.justification ?: "",
            geographicRange = it.geographicRange ?: "",
            population = it.population ?: "",
            habitatAndEcology = it.habitatAndEcology ?: "",
            threats = it.threats ?: "",
            conservationActions = it.conservationActions ?: ""
        )
    }

    override suspend fun persist(input: Long, output: Narrative) {
        val species = box[input]
        species.narrative.target = output
        box.put(species)
    }
}