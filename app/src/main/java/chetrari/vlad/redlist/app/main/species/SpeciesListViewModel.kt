package chetrari.vlad.redlist.app.main.species

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import chetrari.vlad.redlist.base.BaseViewModel
import chetrari.vlad.redlist.data.persistence.model.Country
import chetrari.vlad.redlist.data.persistence.model.Species
import chetrari.vlad.redlist.data.persistence.repository.SpeciesRepository
import chetrari.vlad.redlist.data.persistence.repository.UpdateOption
import chetrari.vlad.redlist.data.persistence.type.Vulnerability
import javax.inject.Inject

class SpeciesListViewModel @Inject constructor(
    private val repository: SpeciesRepository
) : BaseViewModel() {

    private val updateOption = MutableLiveData<UpdateOption<List<Species>>>(UpdateOption.OnEmpty)
    private val country = MutableLiveData<Country>()
    private val vulnerability = MutableLiveData<Vulnerability>()
    private val composite = MediatorLiveData<Composite>().apply {
        addSource(updateOption) { postValue(Composite(updateOption = it)) }
        addSource(country) { postValue(Composite(country = it)) }
        addSource(vulnerability) { postValue(Composite(vulnerability = it)) }
    }
    val species = composite.switchMap { speciesMapper(it.updateOption, it.country, it.vulnerability) }

    fun onFilter(country: Country) = this.country.postValue(country)

    fun onFilter(vulnerability: Vulnerability) = this.vulnerability.postValue(vulnerability)

    fun onRefresh() = updateOption.postValue(UpdateOption.Immediate)

    private fun speciesMapper(updateOption: UpdateOption<List<Species>>, country: Country?, vulnerability: Vulnerability?) =
        when {
            country == null && vulnerability != null -> repository.byVulnerabilityPaged(
                context,
                listConfig,
                updateOption,
                vulnerability
            )
            country != null && vulnerability == null -> repository.byCountryPaged(context, listConfig, updateOption, country)
        country != null && vulnerability != null -> repository.byCountryAndVulnerabilityPaged(
            context,
            listConfig,
            updateOption,
            country,
            vulnerability
        )
        else -> MutableLiveData()
    }

    private inner class Composite(
        val updateOption: UpdateOption<List<Species>> = this.updateOption.value!!,
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