package chetrari.vlad.redlist.data.persistence.repository

import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import chetrari.vlad.redlist.base.Event
import chetrari.vlad.redlist.base.Event.Success
import chetrari.vlad.redlist.data.persistence.repository.UpdateOption.*
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.android.ObjectBoxDataSource
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlin.coroutines.CoroutineContext

abstract class LiveRepository<T>(private val box: Box<T>) {

    protected abstract val defaultOrderByProperty: Property<T>
    protected abstract val idProperty: Property<T>

    private val isEmpty: Boolean
        get() = box.isEmpty
    open val all: List<T>
        get() = box.query { order(defaultOrderByProperty) }.find()

    fun all(context: CoroutineContext, updateOption: UpdateOption<List<T>> = OnEmpty) = liveData<Event<List<T>>>(context) {
        var isAlright = true
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            doCatching(::updateAll)
            isAlright = latestValue !is Event.Error
        }
        when (updateOption) {
            OnEmpty -> if (isEmpty) updater()
            Immediate -> updater()
            is Condition -> if (updateOption.consumer(all)) updater()
        }
        if (isAlright) emitSource(ObjectBoxLiveData(box.query { order(defaultOrderByProperty) }).map { Success(it) })
    }

    operator fun get(id: Long): T? = box[id]

    operator fun get(context: CoroutineContext, id: Long, updateOption: UpdateOption<T> = OnEmpty) = liveData<Event<T>>(context) {
        var isAlright = true
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            doCatching { updateById(id) }
            isAlright = latestValue !is Event.Error
        }
        val entity = get(id)
        when (updateOption) {
            OnEmpty -> if (entity == null) updater()
            Immediate -> updater()
            is Condition -> if (updateOption.consumer(entity)) updater()
        }
        if (isAlright) emitSource(ObjectBoxLiveData(box.query { equal(idProperty, id) }).map { Success(it.first()) })
    }

    protected fun byParamsPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateOption: UpdateOption<List<T>>,
        vararg params: Param<*>
    ) = byQueryPaged(
        context = context, config = config, updateOption = updateOption,
        updateFunction = { params.forEach { it.update() } },
        queryBuilder = {
            val blocks = params.map { it.queryBuilder }
            var builder = this
            if (params.isNotEmpty()) {
                blocks.forEach { builder = it(builder) }
            }
            builder
        })

    protected fun byParams(
        context: CoroutineContext,
        updateOption: UpdateOption<List<T>>,
        vararg params: Param<*>
    ) = byQuery(
        context = context, updateOption = updateOption,
        updateFunction = { params.forEach { it.update() } },
        queryBuilder = {
            val blocks = params.map { it.queryBuilder }
            var builder = this
            if (params.isNotEmpty()) {
                blocks.forEach { builder = it(builder) }
            }
            builder
        })

    private fun byQuery(
        context: CoroutineContext,
        updateOption: UpdateOption<List<T>>,
        updateFunction: suspend () -> Unit,
        queryBuilder: QueryBuilder<T>.() -> QueryBuilder<T>
    ) = liveData<Event<List<T>>>(context) {
        var isAlright = true
        val query = { queryBuilder(box.query()).build() }
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            doCatching(updateFunction)
            isAlright = latestValue !is Event.Error
        }
        when (updateOption) {
            OnEmpty -> if (query().count() == 0L) updater()
            Immediate -> updater()
            is Condition -> if (updateOption.consumer(query().find())) updater()
        }
        if (isAlright) emitSource(ObjectBoxLiveData(query()).map { Success(it) })
    }

    private fun byQueryPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateOption: UpdateOption<List<T>>,
        updateFunction: suspend () -> Unit,
        queryBuilder: QueryBuilder<T>.() -> QueryBuilder<T>
    ) = liveData<Event<PagedList<T>>>(context) {
        var isAlright = true
        val query = { queryBuilder(box.query()).build() }
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            doCatching(updateFunction)
            isAlright = latestValue !is Event.Error
        }
        when (updateOption) {
            OnEmpty -> if (query().count() == 0L) updater()
            Immediate -> updater()
            is Condition -> if (updateOption.consumer(query().find())) updater()
        }
        if (isAlright) {
            val factory = ObjectBoxDataSource.Factory(query())
            val builder = LivePagedListBuilder(factory, config)
            emitSource(builder.build().map { Success(it) })
        }
    }

    protected open suspend fun updateAll() {
        throw RuntimeException("updateAll not defined")
    }

    protected open suspend fun updateById(id: Long) {
        throw RuntimeException("updateById not defined")
    }

    private suspend fun <X> LiveDataScope<Event<X>>.doCatching(block: suspend () -> Unit) {
        try {
            block()
        } catch (t: Throwable) {
            emit(Event.Error(t))
        }
    }

    protected open inner class Param<O>(
        private val obj: O? = null,
        private val updater: suspend (O) -> Unit = {},
        val queryBuilder: QueryBuilder<T>.() -> QueryBuilder<T>
    ) {
        val update: suspend () -> Unit = { obj?.let { updater(it) } }
    }
}