package chetrari.vlad.rts.app.main.species.details

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.repository.SpeciesRepository
import chetrari.vlad.rts.data.persistence.repository.UpdateIf
import javax.inject.Inject

class SpeciesViewModel @Inject constructor(
    private val repository: SpeciesRepository
) : BaseViewModel() {

    private val updateIf = MutableLiveData<UpdateIf<Species>>(UpdateIf.Condition {
        it == null || it.commonName.isBlank() || it.scientificName == it.commonName
    })
    private val speciesId = MutableLiveData<Long>()
    private val composite = MediatorLiveData<Composite>().apply {
        addSource(updateIf) { postValue(Composite(updateIf = it)) }
        addSource(speciesId) { postValue(Composite(speciesId = it)) }
    }
    val species = composite.switchMap { speciesMapper(it.updateIf, it.speciesId) }

    fun onSpeciesIdReceived(id: Long) = speciesId.postValue(id)

    fun onRefresh() = updateIf.postValue(UpdateIf.Refresh)

    private fun speciesMapper(updateIf: UpdateIf<Species>, speciesId: Long?) = when {
        speciesId != null -> repository[context, speciesId, updateIf]
        else -> MutableLiveData()
    }

    private inner class Composite(
        val updateIf: UpdateIf<Species> = this.updateIf.value!!,
        val speciesId: Long? = this.speciesId.value
    )
}
