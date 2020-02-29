package chetrari.vlad.rts.data.network.model

data class WikiMediaResponse(
    val revision: String = "",
    val items: List<WikiMediaBlock> = emptyList()
)
