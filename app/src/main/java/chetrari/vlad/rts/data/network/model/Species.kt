package chetrari.vlad.rts.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Species(
    @SerializedName("taxonid")
    val id: Int = 0,
    @SerializedName("scientific_name")
    val name: String = "",
    val category: String = ""
) : Parcelable {
    var thumbnailImageLink: String? = null
    var fullImageLink: String? = null
}