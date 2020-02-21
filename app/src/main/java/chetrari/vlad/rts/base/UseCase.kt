package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

//TODO move to coroutines instead of liveData output
abstract class UseCase<Input, Output> {

    protected open val dispatcher: CoroutineDispatcher = Dispatchers.Main

    private val liveData = MutableLiveData<Event<Output>>()

    protected abstract suspend fun run(input: Input): Output

    operator fun invoke(scope: CoroutineScope, input: Input): LiveData<Event<Output>> {
        scope.launch { invoke(input) }
        return liveData
    }

    suspend operator fun invoke(input: Input) = liveData.run {
        postValue(Event.Progress())
        try {
            val result = withContext(dispatcher) { run(input) }
            postValue(Event.Success(result))
        } catch (t: Throwable) {
            postValue(Event.Error(t))
        }
    }
}