package chetrari.vlad.rts.data.model.ui

import android.os.Parcelable
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToMany
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Country(
    @Id
    var id: Long = 0,
    @Unique
    var isoCode: String = "",
    var name: String = ""
) : Parcelable {

    @Backlink
    @IgnoredOnParcel
    lateinit var species: ToMany<Species>
}