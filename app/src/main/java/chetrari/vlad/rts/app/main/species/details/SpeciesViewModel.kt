package chetrari.vlad.rts.app.main.species.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.repository.SpeciesLiveRepository
import javax.inject.Inject

class SpeciesViewModel @Inject constructor(
    private val liveRepository: SpeciesLiveRepository
) : BaseViewModel() {

    private val speciesId = MutableLiveData<Long>()
    val species = speciesId.switchMap { id ->
        liveRepository.byId(context, id) { it.commonName.isBlank() }
    }

    fun onSpeciesIdReceived(id: Long) = speciesId.postValue(id)

    fun onRefresh() = speciesId.refresh()
}
