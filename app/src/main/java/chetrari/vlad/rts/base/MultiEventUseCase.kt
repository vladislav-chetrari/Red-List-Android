package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class MultiEventUseCase<in Input, Output> {

    protected open val dispatcher: CoroutineDispatcher = Dispatcher.Main

    protected open val liveData = MutableLiveData<Event<Output>>()

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(input: Input)

    operator fun invoke(scope: CoroutineScope, input: Input): LiveData<Event<Output>> {
        scope.launch { invoke(input) }
        return liveData
    }

    suspend operator fun invoke(input: Input) = liveData.run {
        progress()
        withContext(dispatcher) {
            try {
                execute(input)
            } catch (t: Throwable) {
                postValue(Event.Error(t))
            }
        }
    }

    protected fun update(output: Output) = liveData.postValue(Event.Success(output))

    protected fun progress() = liveData.postValue(Event.Progress)
}

operator fun <R> MultiEventUseCase<Unit, R>.invoke(scope: CoroutineScope): LiveData<Event<R>> = this(scope, Unit)