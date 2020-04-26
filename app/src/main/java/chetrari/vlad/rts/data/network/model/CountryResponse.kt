package chetrari.vlad.rts.data.network.model

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("isocode") val isoCode: String = "",
    @SerializedName("country") val name: String = ""
)
