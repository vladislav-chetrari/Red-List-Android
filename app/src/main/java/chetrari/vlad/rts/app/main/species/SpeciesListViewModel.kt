package chetrari.vlad.rts.app.main.species

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.repository.SpeciesRepository
import chetrari.vlad.rts.data.persistence.type.Vulnerability
import javax.inject.Inject

class SpeciesListViewModel @Inject constructor(
    private val repository: SpeciesRepository
) : BaseViewModel() {

    private val country = MutableLiveData<Country>()
    private val vulnerability = MutableLiveData<Vulnerability>()
    private val composite = MediatorLiveData<CompositeParam>().apply {
        addSource(country) { postValue(compositeParam(country = it)) }
        addSource(vulnerability) { postValue(compositeParam(vulnerability = it)) }
    }

    val species = composite.switchMap { speciesMapper(it.country, it.vulnerability) }

    fun onFilter(country: Country) = this.country.postValue(country)

    fun onFilter(vulnerability: Vulnerability) = this.vulnerability.postValue(vulnerability)

    fun onRefresh() {
        country.refresh()
        vulnerability.refresh()
    }

    private fun compositeParam(
        country: Country? = this.country.value,
        vulnerability: Vulnerability? = this.vulnerability.value
    ) = CompositeParam(country, vulnerability)

    private fun speciesMapper(country: Country?, vulnerability: Vulnerability?) = when {
        country == null && vulnerability != null -> repository.byVulnerabilityPaged(context, listConfig, vulnerability)
        country != null && vulnerability == null -> repository.byCountryPaged(context, listConfig, country)
        country != null && vulnerability != null -> {
            repository.byCountryAndVulnerabilityPaged(context, listConfig, country, vulnerability)
        }
        else -> MutableLiveData()
    }

    private class CompositeParam(
        val country: Country? = null,
        val vulnerability: Vulnerability? = null
    )

    private companion object {
        val listConfig: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(20)
            .setPrefetchDistance(40)
            .build()
    }
}