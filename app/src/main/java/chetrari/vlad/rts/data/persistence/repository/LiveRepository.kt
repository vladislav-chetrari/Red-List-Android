package chetrari.vlad.rts.data.persistence.repository

import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.base.Event.Success
import chetrari.vlad.rts.data.persistence.repository.UpdateIf.*
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.android.ObjectBoxDataSource
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.query.QueryBuilder
import kotlin.coroutines.CoroutineContext

abstract class LiveRepository<T>(private val box: Box<T>) {

    protected open val orderBy: Property<T>? = null

    private val isEmpty: Boolean
        get() = box.isEmpty
    open val all: List<T>
        get() = box.all

    fun all(context: CoroutineContext, updateIf: UpdateIf<List<T>> = Empty) = liveData<Event<List<T>>>(context) {
        var isAlright = true
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            doCatching { updateAll() }
            isAlright = latestValue !is Event.Error
        }
        when (updateIf) {
            Empty -> if (isEmpty) updater()
            Refresh -> updater()
            is Condition -> if (updateIf.consumer(all)) updater()
        }
        if (isAlright) emit(Success(all))
    }

    operator fun get(id: Long): T? = box[id]

    operator fun get(context: CoroutineContext, id: Long, updateIf: UpdateIf<T> = Empty) = liveData<Event<T>>(context) {
        var isAlright = true
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            doCatching { updateById(id) }
            isAlright = latestValue !is Event.Error
        }
        val entity = get(id)
        when (updateIf) {
            Empty -> if (entity == null) updater()
            Refresh -> updater()
            is Condition -> if (updateIf.consumer(entity)) updater()
        }
        if (isAlright) emit(Success(get(id)!!))
    }

    protected fun byParamsPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<T>>,
        vararg params: Param<*>
    ) = byQueryPaged(
        context = context, config = config, updateIf = updateIf,
        updateFunction = { params.forEach { it.update() } },
        queryBuilder = {
            val blocks = params.map { it.queryBuilder }
            var builder = orderBy?.let { order(it) } ?: this
            if (params.isNotEmpty()) {
                blocks.forEach { builder = it(builder) }
            }
            builder
        })

    protected fun byParams(
        context: CoroutineContext,
        updateIf: UpdateIf<List<T>>,
        vararg params: Param<*>
    ) = byQuery(
        context = context, updateIf = updateIf,
        updateFunction = { params.forEach { it.update() } },
        queryBuilder = {
            val blocks = params.map { it.queryBuilder }
            var builder = orderBy?.let { order(it) } ?: this
            if (params.isNotEmpty()) {
                blocks.forEach { builder = it(builder) }
            }
            builder
        })

    private fun byQuery(
        context: CoroutineContext,
        updateIf: UpdateIf<List<T>>,
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
        when (updateIf) {
            Empty -> if (query().count() == 0L) updater()
            Refresh -> updater()
            is Condition -> if (updateIf.consumer(query().find())) updater()
        }
        if (isAlright) emitSource(ObjectBoxLiveData(query()).map { Success(it) })
    }

    private fun byQueryPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<T>>,
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
        when (updateIf) {
            Empty -> if (query().count() == 0L) updater()
            Refresh -> updater()
            is Condition -> if (updateIf.consumer(query().find())) updater()
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