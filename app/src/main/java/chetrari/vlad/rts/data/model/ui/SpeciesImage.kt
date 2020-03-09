package chetrari.vlad.rts.data.model.ui

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class SpeciesImage(
    @Id
    var id: Long = 0,
    val thumbnail: String = "",
    val fullSize: String = ""
)
