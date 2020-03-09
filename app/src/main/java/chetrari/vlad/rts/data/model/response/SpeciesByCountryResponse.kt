package chetrari.vlad.rts.data.model.response

import com.google.gson.annotations.SerializedName

data class SpeciesByCountryResponse(
    val count: Long = 0,
    @SerializedName("country")
    val countryIsoCode: String = "",
    @SerializedName("result")
    val species: List<SpeciesResponse> = emptyList()
)