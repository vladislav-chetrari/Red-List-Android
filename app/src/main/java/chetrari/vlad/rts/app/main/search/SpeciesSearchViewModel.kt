package chetrari.vlad.rts.app.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.repository.CountryRepository
import chetrari.vlad.rts.data.persistence.repository.SpeciesRepository
import chetrari.vlad.rts.data.persistence.repository.UpdateOption
import chetrari.vlad.rts.data.persistence.type.Vulnerability
import javax.inject.Inject

class SpeciesSearchViewModel @Inject constructor(
    countryRepository: CountryRepository,
    private val speciesRepository: SpeciesRepository
) : BaseViewModel() {

    private val countries = MutableLiveData(countryRepository.all)
    private val searchQuery = MutableLiveData(SpeciesRepository.Query())
    val vulnerabilityTypes = mutableLiveData(Vulnerability.values().toList().filter { it != Vulnerability.NE })
    val countryNames = countries.map { it.map(Country::name) }
    val species = searchQuery.switchMap { speciesRepository.byQueryPaged(context, listConfig, UpdateOption.Immediate, it) }

    fun onSearch(speciesName: String, countryNames: List<String>, vulnerability: List<String>) {
        val selectedCountries = countries.value!!.filter { countryNames.contains(it.name) }
        val selectedVulnerability = vulnerabilityTypes.value!!.filter { vulnerability.contains("$it") }
        searchQuery.mutable.postValue(SpeciesRepository.Query(speciesName, selectedCountries, selectedVulnerability))
    }

    fun onRefresh() = searchQuery.rePostValue()

    private companion object {
        val listConfig: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(20)
            .setPrefetchDistance(40)
            .build()
    }
}