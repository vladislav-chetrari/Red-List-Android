package chetrari.vlad.rts.data.repository

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.base.EventLiveData
import chetrari.vlad.rts.data.repository.ObjectBoxRepository.UpdateProperties.Mode.*
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.android.ObjectBoxDataSource
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.query
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.CoroutineScope

open class ObjectBoxRepository<T>(private val box: Box<T>, private val idProperty: Property<T>) {

    val all: List<T>
        get() = box.all

    operator fun get(id: Long): T? = box[id]

    fun liveByID(
        id: Long,
        updateProperties: UpdateProperties
    ): LiveData<Event<T>> {
        val source = ObjectBoxLiveData(box.query { equal(idProperty, id) }).map { it.firstOrNull() }
        val scope = updateProperties.scope
        val liveData = EventLiveData<T>().apply {
            updateProcedure = updateProperties.procedure
            addSource(source) { postValue(if (it == null) null else Event.Success(it)) }
        }
        when (updateProperties.mode) {
            Immediate -> liveData.update(scope)
            OnEmpty -> liveData.observeForever(object : Observer<Event<T>> {
                override fun onChanged(event: Event<T>?) {
                    if (event == null) liveData.update(scope)
                    liveData.removeObserver(this)
                }
            })
            is OnCondition -> {
                if (updateProperties.mode.condition != false) {
                    liveData.update(scope)
                }
            }
        }
        return liveData
    }

    fun query(query: QueryBuilder<T>.() -> Unit): Query<T> = box.query(query)

    fun liveDataByQuery(
        updateProperties: UpdateProperties,
        query: QueryBuilder<T>.() -> Unit
    ): LiveData<Event<List<T>>> = prepareLiveData(updateProperties,
        MediatorLiveData<List<T>>().apply { addSource(ObjectBoxLiveData(box.query(query)), ::postValue) })

    fun pagedLiveDataByQuery(
        updateProperties: UpdateProperties,
        config: PagedList.Config,
        query: QueryBuilder<T>.() -> Unit
    ): LiveData<Event<PagedList<T>>> = prepareLiveData(
        updateProperties,
        LivePagedListBuilder(ObjectBoxDataSource.Factory(box.query(query)), config).build().map { it } as MutableLiveData
    )

    private fun <X : List<T>> prepareLiveData(
        updateProperties: UpdateProperties,
        source: MutableLiveData<X>
    ): LiveData<Event<X>> {
        val scope = updateProperties.scope
        val liveData = EventLiveData<X>().apply {
            updateProcedure = updateProperties.procedure
            addSource(source) { postValue(Event.Success(it)) }
        }
        when (updateProperties.mode) {
            Immediate -> liveData.update(scope)
            OnEmpty -> liveData.observeForever(object : Observer<Event<List<T>>> {
                override fun onChanged(event: Event<List<T>>?) {
                    if (event is Event.Success && event.result.isEmpty()) liveData.update(scope)
                    liveData.removeObserver(this)
                }
            })
            is OnCondition -> {
                if (updateProperties.mode.condition != false) {
                    liveData.update(scope)
                }
            }
        }
        return liveData
    }

    class UpdateProperties(
        val mode: Mode = OnEmpty,
        val scope: CoroutineScope,
        val procedure: suspend () -> Unit
    ) {
        sealed class Mode {
            object Immediate : Mode()
            object OnEmpty : Mode()
            class OnCondition(val condition: Boolean? = null) : Mode()
        }
    }
}