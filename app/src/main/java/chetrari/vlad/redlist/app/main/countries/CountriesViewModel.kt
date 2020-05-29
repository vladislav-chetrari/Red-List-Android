package chetrari.vlad.redlist.app.main.countries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import chetrari.vlad.redlist.base.BaseViewModel
import chetrari.vlad.redlist.data.persistence.model.Country
import chetrari.vlad.redlist.data.persistence.repository.CountryRepository
import chetrari.vlad.redlist.data.persistence.repository.UpdateOption
import javax.inject.Inject

class CountriesViewModel @Inject constructor(
    private val repository: CountryRepository
) : BaseViewModel() {

    private val updateIf = MutableLiveData<UpdateOption<List<Country>>>(UpdateOption.OnEmpty)
    val countries = updateIf.switchMap { repository.all(context, it) }

    fun onRefresh() = updateIf.postValue(UpdateOption.Immediate)
}