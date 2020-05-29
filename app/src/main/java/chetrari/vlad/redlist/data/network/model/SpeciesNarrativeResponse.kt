package chetrari.vlad.redlist.data.network.model

import com.google.gson.annotations.SerializedName

//TODO figure out gson null serialization
data class SpeciesNarrativeResponse(
    @SerializedName("species_id")
    val speciesId: Long = 0,
    @SerializedName("taxonomicnotes")
    val taxonomicNotes: String? = "",
    @SerializedName("rationale")
    val justification: String? = "",
    @SerializedName("geographicrange")
    val geographicRange: String? = "",
    val population: String? = "",
    @SerializedName("habitat")
    val habitatAndEcology: String? = "",
    val threats: String? = "",
    @SerializedName("conservationmeasures")
    val conservationActions: String? = ""
)
