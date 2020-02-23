package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO refactor use case to use it as many times as I want (single or multiple)
abstract class SingleEventUseCase<in Input, Output> {

    protected open val dispatcher: CoroutineDispatcher = Dispatcher.Main

    protected open val liveData = MutableLiveData<Event<Output>>()

    @Throws(RuntimeException::class)
    abstract suspend fun execute(input: Input): Output

    operator fun invoke(scope: CoroutineScope, input: Input): LiveData<Event<Output>> {
        scope.launch { invoke(input) }
        return liveData
    }

    suspend operator fun invoke(input: Input) = liveData.run {
        liveData.postValue(Event.Progress)
        try {
            val result = withContext(dispatcher) { execute(input) }
            liveData.postValue(Event.Success(result))
        } catch (t: Throwable) {
            liveData.postValue(Event.Error(t))
        }
    }
}

operator fun <Output> SingleEventUseCase<Unit, Output>.invoke(scope: CoroutineScope): LiveData<Event<Output>> = this(scope, Unit)