package chetrari.vlad.rts.data.network.fetch

import chetrari.vlad.rts.data.model.response.WikiMediaBlockResponse
import chetrari.vlad.rts.data.model.ui.Species
import chetrari.vlad.rts.data.model.ui.SpeciesImage
import chetrari.vlad.rts.data.network.api.WikiMediaApi
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesImageLinkUpdater @Inject constructor(
    private val api: WikiMediaApi,
    private val box: Box<Species>
) : Fetcher<Species, List<WikiMediaBlockResponse>, List<SpeciesImage>>() {

    override suspend fun fetch(input: Species) = input.scientificName.trim().replace(" ", "_").let {
        api.media(it).body.items
    }

    override suspend fun map(response: List<WikiMediaBlockResponse>) = response
        .map { SpeciesImage(thumbnail = it.thumbnail.url, fullSize = it.original.url) }

    override suspend fun persist(input: Species, output: List<SpeciesImage>) {
        if (output.isEmpty() || input.images.isNotEmpty()) return
        output.forEach { input.images.add(it) }
        box.put(input)
    }
}