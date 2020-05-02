package chetrari.vlad.rts.app.main.species.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.persistence.repository.SpeciesRepository
import javax.inject.Inject

class SpeciesViewModel @Inject constructor(
    private val repository: SpeciesRepository
) : BaseViewModel() {

    private val speciesId = MutableLiveData<Long>()
    val species = speciesId.switchMap { id ->
        repository.byId(context, id) { it.commonName.isBlank() }
    }

    fun onSpeciesIdReceived(id: Long) = speciesId.postValue(id)

    fun onRefresh() = speciesId.refresh()
}
