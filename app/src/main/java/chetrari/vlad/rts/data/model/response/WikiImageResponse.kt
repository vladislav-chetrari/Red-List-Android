package chetrari.vlad.rts.data.model.response

import com.google.gson.annotations.SerializedName

data class WikiImageResponse(
    @SerializedName("source")
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
)
