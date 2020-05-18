package chetrari.vlad.rts.app.main.species

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.repository.SpeciesRepository
import chetrari.vlad.rts.data.persistence.repository.UpdateIf
import chetrari.vlad.rts.data.persistence.type.Vulnerability
import javax.inject.Inject

class SpeciesListViewModel @Inject constructor(
    private val repository: SpeciesRepository
) : BaseViewModel() {

    private val updateIf = MutableLiveData<UpdateIf<List<Species>>>(UpdateIf.Empty)
    private val country = MutableLiveData<Country>()
    private val vulnerability = MutableLiveData<Vulnerability>()
    private val composite = MediatorLiveData<Composite>().apply {
        addSource(updateIf) { postValue(Composite(updateIf = it)) }
        addSource(country) { postValue(Composite(country = it)) }
        addSource(vulnerability) { postValue(Composite(vulnerability = it)) }
    }
    val species = composite.switchMap { speciesMapper(it.updateIf, it.country, it.vulnerability) }

    fun onFilter(country: Country) = this.country.postValue(country)

    fun onFilter(vulnerability: Vulnerability) = this.vulnerability.postValue(vulnerability)

    fun onRefresh() = updateIf.postValue(UpdateIf.Refresh)

    private fun speciesMapper(updateIf: UpdateIf<List<Species>>, country: Country?, vulnerability: Vulnerability?) = when {
        country == null && vulnerability != null -> repository.byVulnerabilityPaged(context, listConfig, updateIf, vulnerability)
        country != null && vulnerability == null -> repository.byCountryPaged(context, listConfig, updateIf, country)
        country != null && vulnerability != null -> repository.byCountryAndVulnerabilityPaged(
            context,
            listConfig,
            updateIf,
            country,
            vulnerability
        )
        else -> MutableLiveData()
    }

    private inner class Composite(
        val updateIf: UpdateIf<List<Species>> = this.updateIf.value!!,
        val country: Country? = this.country.value,
        val vulnerability: Vulnerability? = this.vulnerability.value
    )

    private companion object {
        val listConfig: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(20)
            .setPrefetchDistance(40)
            .build()
    }
}