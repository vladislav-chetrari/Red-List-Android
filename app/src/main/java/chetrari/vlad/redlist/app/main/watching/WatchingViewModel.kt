package chetrari.vlad.redlist.app.main.watching

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import chetrari.vlad.redlist.base.BaseViewModel
import chetrari.vlad.redlist.data.persistence.model.Species
import chetrari.vlad.redlist.data.persistence.repository.SpeciesRepository
import chetrari.vlad.redlist.data.persistence.repository.UpdateOption
import javax.inject.Inject

class WatchingViewModel @Inject constructor(
    private val repository: SpeciesRepository
) : BaseViewModel() {

    private val updateOption = MutableLiveData<UpdateOption<List<Species>>>(UpdateOption.None)
    private val composite = MediatorLiveData<Composite>().apply {
        addSource(updateOption) { postValue(Composite(updateOption = it)) }
    }
    val species = composite.switchMap { repository.watchingSpeciesPaged(context, listConfig, it.updateOption) }

    fun onRefresh() = updateOption.postValue(UpdateOption.Immediate)

    private inner class Composite(
        val updateOption: UpdateOption<List<Species>> = this.updateOption.value!!
    )

    private companion object {
        val listConfig: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(20)
            .setPrefetchDistance(40)
            .build()
    }
}