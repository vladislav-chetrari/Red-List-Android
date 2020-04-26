package chetrari.vlad.rts.data.network.model

import com.google.gson.annotations.SerializedName

data class CommonNameResponse(
    @SerializedName("taxonname")
    val name: String = "",
    val language: String = ""
)
