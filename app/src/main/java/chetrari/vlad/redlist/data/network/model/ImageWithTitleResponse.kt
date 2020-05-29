package chetrari.vlad.redlist.data.network.model

import com.google.gson.annotations.SerializedName

data class ImageWithTitleResponse(
    val title: String = "",
    @SerializedName("pagemap")
    val image: ImageResponse = ImageResponse()
)
