package chetrari.vlad.rts.data.persistence.repository

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.data.persistence.repository.UpdateIf.*
import io.objectbox.Box
import io.objectbox.android.ObjectBoxDataSource
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlin.coroutines.CoroutineContext

abstract class LiveRepository<T>(private val box: Box<T>) {

    protected open val defaultParams = emptyList<Param<*>>()
    private val isEmpty: Boolean
        get() = box.isEmpty
    open val all: List<T>
        get() = box.all

    fun all(context: CoroutineContext, updateIf: UpdateIf<List<T>> = Empty) = liveData(context) {
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            updateAll()
        }
        when (updateIf) {
            Empty -> if (isEmpty) updater()
            Refresh -> updater()
            is Condition -> if (updateIf.consumer(all)) updater()
        }
        emit(Event.Success(all))
    }

    operator fun get(id: Long): T? = box[id]

    operator fun get(context: CoroutineContext, id: Long, updateIf: UpdateIf<T> = Empty) = liveData<Event<T>>(context) {
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            updateById(id)
        }
        val entity = get(id)
        when (updateIf) {
            Empty -> if (entity == null) updater()
            Refresh -> updater()
            is Condition -> if (updateIf.consumer(entity)) updater()
        }
        emit(Event.Success(get(id)!!))
    }

    protected fun byParamsPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<T>>,
        vararg params: Param<*>
    ) = byQueryPaged(
        context = context, config = config, updateIf = updateIf,
        updateFunction = { (defaultParams + params).forEach { it.update() } },
        dbQuery = { (defaultParams + params).forEach { it.queryBuilder(this) } })

    protected fun byParams(
        context: CoroutineContext,
        updateIf: UpdateIf<List<T>>,
        vararg params: Param<*>
    ) = byQuery(
        context = context, updateIf = updateIf,
        updateFunction = { (defaultParams + params).forEach { it.update() } },
        dbQuery = { (defaultParams + params).forEach { it.queryBuilder(this) } })

    private fun byQuery(
        context: CoroutineContext,
        updateIf: UpdateIf<List<T>>,
        updateFunction: suspend () -> Unit,
        dbQuery: QueryBuilder<T>.() -> Unit
    ) = liveData<Event<List<T>>>(context) {
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            updateFunction()
        }
        when (updateIf) {
            Empty -> if (box.query(dbQuery).count() == 0L) updater()
            Refresh -> updater()
            is Condition -> if (updateIf.consumer(box.query(dbQuery).find())) updater()
        }
        emit(Event.Success(box.query(dbQuery).find()))
    }

    private fun byQueryPaged(
        context: CoroutineContext,
        config: PagedList.Config,
        updateIf: UpdateIf<List<T>>,
        updateFunction: suspend () -> Unit,
        dbQuery: QueryBuilder<T>.() -> Unit
    ) = liveData<Event<PagedList<T>>>(context) {
        val updater: suspend () -> Unit = {
            emit(Event.Progress)
            updateFunction()
        }
        when (updateIf) {
            Empty -> if (box.query(dbQuery).count() == 0L) updater()
            Refresh -> updater()
            is Condition -> if (updateIf.consumer(box.query(dbQuery).find())) updater()
        }
        val factory = ObjectBoxDataSource.Factory(box.query(dbQuery))
        val builder = LivePagedListBuilder(factory, config)
        emitSource(builder.build().map { Event.Success(it) })
    }

    protected open suspend fun updateAll() {
        throw RuntimeException("updateAll not defined")
    }

    protected open suspend fun updateById(id: Long) {
        throw RuntimeException("updateById not defined")
    }

    protected open inner class Param<O>(
        private val obj: O? = null,
        private val updater: suspend (O) -> Unit = {},
        val queryBuilder: QueryBuilder<T>.() -> Unit
    ) {
        val update: suspend () -> Unit = { obj?.let { updater(it) } }
    }

    protected inner class QueryParam(builder: QueryBuilder<T>.() -> Unit) : Param<Unit>(queryBuilder = builder)
}