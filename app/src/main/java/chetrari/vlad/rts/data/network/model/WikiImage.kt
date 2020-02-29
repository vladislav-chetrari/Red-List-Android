package chetrari.vlad.rts.data.network.model

import com.google.gson.annotations.SerializedName

data class WikiImage(
    @SerializedName("source")
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
)
