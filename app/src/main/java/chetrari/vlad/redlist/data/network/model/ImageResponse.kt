package chetrari.vlad.redlist.data.network.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("cse_thumbnail")
    val thumbnail: List<LinkContainer> = emptyList(),
    @SerializedName("cse_image")
    val fullSize: List<LinkContainer> = emptyList()
)
