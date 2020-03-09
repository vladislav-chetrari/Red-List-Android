package chetrari.vlad.rts.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO remove
abstract class UseCase<in Input, Output> {

    protected open val dispatcher: CoroutineDispatcher = Dispatcher.Main

    @Throws(RuntimeException::class)
    abstract suspend fun execute(input: Input): Output

    operator fun invoke(scope: CoroutineScope, input: Input): LiveData<Event<Output>> = MutableLiveData<Event<Output>>().also {
        scope.launch {
            it.run {
                postValue(Event.Progress)
                try {
                    val result = withContext(dispatcher) { execute(input) }
                    postValue(Event.Success(result))
                } catch (t: Throwable) {
                    postValue(Event.Error(t))
                }
            }
        }
    }
}

operator fun <Output> UseCase<Unit, Output>.invoke(scope: CoroutineScope) = this(scope, Unit)