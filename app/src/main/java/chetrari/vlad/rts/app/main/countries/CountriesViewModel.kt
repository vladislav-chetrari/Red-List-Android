package chetrari.vlad.rts.app.main.countries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.persistence.model.Country
import chetrari.vlad.rts.data.persistence.repository.CountryRepository
import chetrari.vlad.rts.data.persistence.repository.UpdateIf
import javax.inject.Inject

class CountriesViewModel @Inject constructor(
    private val repository: CountryRepository
) : BaseViewModel() {

    private val updateIf = MutableLiveData<UpdateIf<List<Country>>>(UpdateIf.Empty)
    val countries = updateIf.switchMap { repository.all(context, it) }

    fun onRefresh() = updateIf.postValue(UpdateIf.Refresh)
}