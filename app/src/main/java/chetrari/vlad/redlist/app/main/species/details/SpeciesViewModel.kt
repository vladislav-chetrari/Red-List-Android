package chetrari.vlad.redlist.app.main.species.details

import androidx.lifecycle.*
import chetrari.vlad.redlist.base.BaseViewModel
import chetrari.vlad.redlist.base.Event
import chetrari.vlad.redlist.data.persistence.model.Species
import chetrari.vlad.redlist.data.persistence.repository.SpeciesRepository
import chetrari.vlad.redlist.data.persistence.repository.UpdateOption
import kotlinx.coroutines.launch
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

    fun onSpeciesHtmlReceive(html: String) {
        val speciesId = (species.value as Event.Success).result.id
        viewModelScope.launch {
            repository.updateImagesFromHtml(context, speciesId, html)
        }
    }

    private inner class Composite(
        val updateOption: UpdateOption<Species> = this.updateOption.value!!,
        val speciesId: Long? = this.speciesId.value
    )
}
