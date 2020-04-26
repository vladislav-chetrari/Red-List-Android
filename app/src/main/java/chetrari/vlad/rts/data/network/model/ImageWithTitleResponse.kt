package chetrari.vlad.rts.data.network.model

import com.google.gson.annotations.SerializedName

data class ImageWithTitleResponse(
    val title: String = "",
    @SerializedName("pagemap")
    val image: ImageResponse = ImageResponse()
)
