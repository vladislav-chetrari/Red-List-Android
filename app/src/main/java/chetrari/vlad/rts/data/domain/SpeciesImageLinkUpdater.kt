package chetrari.vlad.rts.data.domain

import chetrari.vlad.rts.data.network.api.WikiMediaApi
import chetrari.vlad.rts.data.network.model.Species
import javax.inject.Inject

class SpeciesImageLinkUpdater @Inject constructor(
    private val api: WikiMediaApi
) : IOUseCase<Species, Species>() {

    override suspend fun execute(input: Species): Species {
        api.media(input.name.trim().replace(" ", "_")).body.items.firstOrNull()?.let {
            input.thumbnailImageLink = it.thumbnail.url
            input.fullImageLink = it.original.url
        }
        return input
    }
}