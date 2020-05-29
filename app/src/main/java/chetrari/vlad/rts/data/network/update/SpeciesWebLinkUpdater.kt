package chetrari.vlad.rts.data.network.update

import chetrari.vlad.rts.data.network.api.RedListApi
import chetrari.vlad.rts.data.network.model.SpeciesWebLinkResponse
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.model.Species_
import io.objectbox.Box
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder.StringOrder.CASE_INSENSITIVE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesWebLinkUpdater @Inject constructor(
    private val api: RedListApi,
    private val box: Box<Species>
) : DataUpdater<String, SpeciesWebLinkResponse, String>() {

    override suspend fun fetch(input: String) = api.webLinkBySpeciesScientificName(input.trim()).body

    override suspend fun map(input: String, response: SpeciesWebLinkResponse) = response.url

    override suspend fun persist(input: String, output: String) {
        val species = box.query { equal(Species_.scientificName, input, CASE_INSENSITIVE) }.findFirst() ?: return
        species.webLink = output
        box.put(species)
    }
}