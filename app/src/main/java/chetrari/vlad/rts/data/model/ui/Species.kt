package chetrari.vlad.rts.data.model.ui

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Species(
    @Id
    var id: Long = 0,
    var taxonId: Int = 0,
    var scientificName: String = "",
    var category: String = ""
) {
    lateinit var images: ToMany<SpeciesImage>
    lateinit var countries: ToMany<Country>
}