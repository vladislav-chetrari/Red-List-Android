package chetrari.vlad.redlist.data.network.model

import com.google.gson.annotations.SerializedName

data class SpeciesWebLinkResponse(
    @SerializedName("rlurl")
    val url: String = ""
)
