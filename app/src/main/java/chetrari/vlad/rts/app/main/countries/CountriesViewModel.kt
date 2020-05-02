package chetrari.vlad.rts.app.main.countries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.persistence.repository.CountryRepository
import javax.inject.Inject


class CountriesViewModel @Inject constructor(
    private val repository: CountryRepository
) : BaseViewModel() {

    private val refreshTrigger = MutableLiveData(Unit)
    val countries = refreshTrigger.switchMap {
        repository.all(context)
    }

    fun onRefresh() = refreshTrigger.postValue(Unit)
}