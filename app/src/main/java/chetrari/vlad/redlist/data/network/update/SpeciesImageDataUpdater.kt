package chetrari.vlad.redlist.data.network.update

import chetrari.vlad.redlist.data.persistence.model.Species
import chetrari.vlad.redlist.data.persistence.model.SpeciesImage
import io.objectbox.Box
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

typealias SpeciesIdHtmlPair = Pair<Long, String>

@Singleton
class SpeciesImageDataUpdater @Inject constructor(
    private val box: Box<Species>
) : DataUpdater<SpeciesIdHtmlPair, List<String>, List<SpeciesImage>>() {

    override suspend fun fetch(input: SpeciesIdHtmlPair) = Jsoup.parse(input.second).body()
        ?.getElementById("page")
        ?.getElementById("content")
        ?.getElementsByTag("main")?.firstOrNull()
        ?.getElementById("redlist-js")
        ?.getElementsByClass("page-species")?.firstOrNull()
        ?.getElementsByTag("header")?.firstOrNull()
        ?.getElementsByClass("layout-page layout-page--image")?.firstOrNull()
        ?.getElementsByClass("layout-page--image__minor")?.firstOrNull()
        ?.getElementsByClass("layout-container featherlight__gallery")?.firstOrNull()
        ?.getElementsByTag("a")
        ?.map { it.attr("href") }
        ?: emptyList()

    override suspend fun map(input: SpeciesIdHtmlPair, response: List<String>) =
        response.map { SpeciesImage(url = it) }

    override suspend fun persist(input: SpeciesIdHtmlPair, output: List<SpeciesImage>) {
        if (output.isEmpty()) return
        val species = box[input.first]
        species.images.clear()
        species.images.addAll(output)
        box.put(species)
    }
}