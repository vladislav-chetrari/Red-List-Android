package chetrari.vlad.rts.data.model.response

import com.google.gson.annotations.SerializedName

data class SpeciesResponse(
    @SerializedName("taxonid")
    val taxonId: Int = 0,
    @SerializedName("scientific_name")
    val scientificName: String = "",
    val category: String = ""
)