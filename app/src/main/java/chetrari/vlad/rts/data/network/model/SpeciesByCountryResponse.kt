package chetrari.vlad.rts.data.network.model

import com.google.gson.annotations.SerializedName

data class SpeciesByCountryResponse(
    val count: Long = 0,
    @SerializedName("country")
    val countryIsoCode: String = "",
    @SerializedName("result")
    val species: List<Species> = emptyList()
)