package chetrari.vlad.rts.data.network.fetch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chetrari.vlad.rts.base.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call

abstract class Fetcher<in Input, Response, Output> {

    @Throws(RuntimeException::class)
    protected abstract suspend fun fetch(input: Input): Response

    @Throws(RuntimeException::class)
    protected abstract suspend fun map(response: Response): Output

    @Throws(RuntimeException::class)
    protected abstract suspend fun persist(input: Input, output: Output)

    operator fun invoke(scope: CoroutineScope, input: Input): LiveData<Event> {
        val liveData = MutableLiveData<Event>(Event.Progress)
        scope.launch(context = Dispatcher.IO) {
            var error: Throwable? = null
            try {
                val response = fetch(input)
                val output = map(response)
                persist(input, output)
            } catch (t: Throwable) {
                error = t
            } finally {
                liveData.postValue(Event.Complete(error))
            }
        }
        return liveData
    }

    protected val <T> Call<T>.body: T
        get() {
            val response = execute()
            if (response.isSuccessful) return response.body()!!
            else throw Throwable(response.errorBody()?.string())
        }

    sealed class Event {
        object Progress : Event()
        data class Complete(val error: Throwable? = null) : Event()
    }
}

operator fun <Response, Output> Fetcher<Unit, Response, Output>.invoke(scope: CoroutineScope) = invoke(scope, Unit)