package chetrari.vlad.redlist.data.persistence.model

import android.os.Parcelable
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class SpeciesImage(
    @Id
    var id: Long = 0,
    var thumbnail: String = "",
    var fullSize: String = ""
) : Parcelable
