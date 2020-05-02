package chetrari.vlad.rts.app.main.species

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.repository.SpeciesRepository
import javax.inject.Inject

class SpeciesListViewModel @Inject constructor(
    private val repository: SpeciesRepository
) : BaseViewModel() {

    private val selectedCountry = MutableLiveData<Country>()
    val species = selectedCountry.switchMap {
        repository.byCountryPaged(context, pagedListConfig, it)
    }

    fun onSearchByCountry(country: Country) = selectedCountry.postValue(country)

    fun onRefresh() = selectedCountry.refresh()

    private companion object {
        val pagedListConfig: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(20)
            .setPrefetchDistance(40)
            .build()
    }
}