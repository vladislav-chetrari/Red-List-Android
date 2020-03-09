package chetrari.vlad.rts.data.model.response

import chetrari.vlad.rts.data.model.response.WikiImageResponse

data class WikiMediaBlockResponse(
    val thumbnail: WikiImageResponse = WikiImageResponse(),
    val original: WikiImageResponse = WikiImageResponse()
)
