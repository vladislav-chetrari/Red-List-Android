package chetrari.vlad.rts.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    @SerializedName("isocode")
    val isoCode: String = "",
    @SerializedName("country")
    val name: String = ""
) : Parcelable {
    val flagImageLink: String
        get() = "https://www.countryflags.io/$isoCode/shiny/64.png"
}
