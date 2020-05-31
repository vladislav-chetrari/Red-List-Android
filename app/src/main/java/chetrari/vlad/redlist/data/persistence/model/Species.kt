package chetrari.vlad.redlist.data.persistence.model

import chetrari.vlad.redlist.data.persistence.model.converter.VulnerabilityConverter
import chetrari.vlad.redlist.data.persistence.type.Vulnerability
import chetrari.vlad.redlist.data.persistence.type.Vulnerability.NE
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@Entity
data class Species(
    @Id(assignable = true)
    var id: Long = 0,
    @Unique
    var scientificName: String = "",
    @Convert(converter = VulnerabilityConverter::class, dbType = String::class)
    var category: Vulnerability = NE,
    var kingdom: String = "",
    var phylum: String = "",
    var bioClass: String = "",
    var order: String = "",
    var family: String = "",
    var genus: String = "",
    var commonName: String = "",
    var populationTrend: String = "",
    var watching: Boolean = false,
    var webLink: String = ""
) {
    lateinit var images: ToMany<SpeciesImage>
    lateinit var countries: ToMany<Country>
    lateinit var narrative: ToOne<Narrative>
}