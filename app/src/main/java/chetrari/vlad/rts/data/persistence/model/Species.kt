package chetrari.vlad.rts.data.persistence.model

import chetrari.vlad.rts.data.persistence.type.Vulnerability
import chetrari.vlad.rts.data.persistence.type.Vulnerability.NE
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.converter.PropertyConverter
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@Entity
data class Species(
    @Id(assignable = true)
    var id: Long = 0,
    @Unique
    var scientificName: String = "",
    @Convert(converter = VulnerabilityConverter::class, dbType = Int::class)
    var category: Vulnerability = NE,
    var kingdom: String = "",
    var phylum: String = "",
    var bioClass: String = "",
    var order: String = "",
    var family: String = "",
    var genus: String = "",
    var commonName: String = "",
    var populationTrend: String = ""
) {
    lateinit var images: ToMany<SpeciesImage>
    lateinit var countries: ToMany<Country>
    lateinit var narrative: ToOne<Narrative>
}

class VulnerabilityConverter : PropertyConverter<Vulnerability, Int> {
    override fun convertToEntityProperty(databaseValue: Int?) = databaseValue?.let {
        Vulnerability.values()[it]
    } ?: NE

    override fun convertToDatabaseValue(entityProperty: Vulnerability?) = entityProperty?.ordinal ?: 0
}