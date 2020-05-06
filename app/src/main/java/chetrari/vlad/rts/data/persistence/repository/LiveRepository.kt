package chetrari.vlad.rts.data.persistence.repository

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import chetrari.vlad.rts.base.Event
import io.objectbox.Box
import io.objectbox.android.ObjectBoxDataSource
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import kotlin.coroutines.CoroutineContext

abstract class LiveRepository<T>(private val box: Box<T>) {

    protected open val all: List<T>
        get() = box.all

    protected open val defaultParams: List<Param<*>>
        get() = emptyList()

    fun all(context: CoroutineContext) = liveData(context) {
        if (box.isEmpty) {
            emit(Event.Progress)
            updateAll()
        }
        emit(Event.Success(all))
    }

    fun byId(context: CoroutineContext, id: Long, updateIf: (T) -> Boolean = { false }) = liveData<Event<T>>(context) {
        val entity = box[id]
        if (entity == null || updateIf(entity)) {
            emit(Event.Progress)
            updateById(id)
        }
        emit(Event.Success(box[id]))
    }

    fun byParams(context: CoroutineContext, config: PagedList.Config, vararg params: Param<*>) = byQueryPagedOrUpdate(
        context = context,
        config = config,
        updateFunction = { (defaultParams + params).forEach { it.update() } },
        dbQuery = { (defaultParams + params).forEach { it.queryBuilder(this) } })

    fun byParams(context: CoroutineContext, vararg params: Param<*>) = byQueryOrUpdate(
        context = context,
        updateFunction = { (defaultParams + params).forEach { it.update() } },
        dbQuery = { (defaultParams + params).forEach { it.queryBuilder(this) } })

    private fun byQueryOrUpdate(
        context: CoroutineContext,
        updateFunction: suspend () -> Unit,
        dbQuery: QueryBuilder<T>.() -> Unit
    ) = liveData<Event<List<T>>>(context) {
        if (box.query(dbQuery).count() == 0L) {
            emit(Event.Progress)
            updateFunction()
        }
        emit(Event.Success(box.query(dbQuery).find()))
    }

    private fun byQueryPagedOrUpdate(
        context: CoroutineContext,
        config: PagedList.Config,
        updateFunction: suspend () -> Unit,
        dbQuery: QueryBuilder<T>.() -> Unit
    ) = liveData<Event<PagedList<T>>>(context) {
        if (box.query(dbQuery).count() == 0L) {
            emit(Event.Progress)
            updateFunction()
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

    open inner class Param<O>(
        private val obj: O? = null,
        private val updater: suspend (O) -> Unit = {},
        val queryBuilder: QueryBuilder<T>.() -> Unit
    ) {
        val update: suspend () -> Unit = { obj?.let { updater(it) } }
    }

    inner class QueryParam(builder: QueryBuilder<T>.() -> Unit) : Param<Unit>(queryBuilder = builder)
}