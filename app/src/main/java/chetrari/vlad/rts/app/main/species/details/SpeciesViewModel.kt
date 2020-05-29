package chetrari.vlad.rts.app.main.species.details

import androidx.lifecycle.*
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.repository.SpeciesRepository
import chetrari.vlad.rts.data.persistence.repository.UpdateOption
import javax.inject.Inject

class SpeciesViewModel @Inject constructor(
    private val repository: SpeciesRepository
) : BaseViewModel() {

    private val updateOption = MutableLiveData<UpdateOption<Species>>(UpdateOption.Condition {
        it == null || it.kingdom.isEmpty()
    })
    private val speciesId = MutableLiveData<Long>()
    private val composite = MediatorLiveData<Composite>().apply {
        addSource(updateOption) { postValue(Composite(updateOption = it)) }
        addSource(speciesId) { postValue(Composite(speciesId = it)) }
    }
    val species = composite.switchMap { speciesMapper(it.updateOption, it.speciesId) }
    private val loadedSpecies = species.map { if (it is Event.Success) it.result else null }

    fun onSpeciesIdReceived(id: Long) = speciesId.postValue(id)

    fun onRefresh() = updateOption.postValue(UpdateOption.Immediate)

    private fun speciesMapper(updateOption: UpdateOption<Species>, speciesId: Long?) = when {
        speciesId != null -> repository[context, speciesId, updateOption]
        else -> MutableLiveData()
    }

    fun onWatch() = species.observeForever(object : Observer<Event<Species>> {
        override fun onChanged(event: Event<Species>) {
            if (event is Event.Success) {
                repository.toggleWatching(event.result)
                species.removeObserver(this)
            }
        }
    })

    private inner class Composite(
        val updateOption: UpdateOption<Species> = this.updateOption.value!!,
        val speciesId: Long? = this.speciesId.value
    )
}
