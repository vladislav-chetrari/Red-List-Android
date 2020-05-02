package chetrari.vlad.rts.data.network.model

import chetrari.vlad.rts.data.persistence.type.Vulnerability
import com.google.gson.annotations.SerializedName

data class SpeciesResponse(
    @SerializedName("taxonid")
    val taxonId: Long = 0,
    @SerializedName("scientific_name")
    val scientificName: String = "",
    val category: Vulnerability? = Vulnerability.NE,
    val kingdom: String? = "",
    val phylum: String? = "",
    @SerializedName("class")
    val bioClass: String? = "",
    val order: String? = "",
    val family: String? = "",
    val genus: String? = "",
    @SerializedName("main_common_name")
    val commonName: String? = "",
    @SerializedName("population_trend")
    val populationTrend: String? = ""
)