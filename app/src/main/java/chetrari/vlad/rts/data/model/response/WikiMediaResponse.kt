package chetrari.vlad.rts.data.model.response

import chetrari.vlad.rts.data.model.response.WikiMediaBlockResponse

data class WikiMediaResponse(
    val revision: String = "",
    val items: List<WikiMediaBlockResponse> = emptyList()
)
