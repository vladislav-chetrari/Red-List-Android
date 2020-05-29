package chetrari.vlad.redlist.data.persistence.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Narrative(
    @Id
    var id: Long = 0,
    var taxonomicNotes: String = "",
    var justification: String = "",
    var geographicRange: String = "",
    var population: String = "",
    var habitatAndEcology: String = "",
    var threats: String = "",
    var conservationActions: String = ""
)