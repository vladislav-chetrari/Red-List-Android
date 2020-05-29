package chetrari.vlad.rts.data.network.update

import chetrari.vlad.rts.data.network.api.GoogleCustomSearchApi
import chetrari.vlad.rts.data.network.model.ImageWithTitleResponse
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.model.SpeciesImage
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeciesImageDataUpdater @Inject constructor(
    private val api: GoogleCustomSearchApi,
    private val box: Box<Species>
) : DataUpdater<Long, List<ImageWithTitleResponse>, List<SpeciesImage>>() {

    override suspend fun fetch(input: Long): List<ImageWithTitleResponse> {
        val species = box[input]
        val query = if (species.commonName.isNotBlank()) species.commonName else species.scientificName
        return api.searchImages(query).body.items
    }

    override suspend fun map(input: Long, response: List<ImageWithTitleResponse>): List<SpeciesImage> {
        val species = box[input]
        val commonName = species.commonName
        val scientificName = species.scientificName
        return response.asSequence()
            .filter { it.title.isNotBlank() }
            .filter { with(it.title) { commonName.isNotBlank() && contains(commonName, true) || contains(scientificName, true) } }
            .map { it.image }
            .map { (it.thumbnail.firstOrNull()?.src ?: "") to (it.fullSize.firstOrNull()?.src ?: "") }
            .filter { it.first.isNotBlank() || it.second.isNotBlank() }
            .map {
                if (it.first.isBlank() && it.second.isNotBlank()) it.second to it.second
                else if (it.first.isNotBlank() && it.second.isBlank()) it.first to it.first
                else it
            }
            .map { SpeciesImage(thumbnail = it.first, fullSize = it.second) }
            .toList()
    }

    override suspend fun persist(input: Long, output: List<SpeciesImage>) {
        val species = box[input]
        species.images.clear()
        species.images.addAll(output)
        box.put(species)
    }
}