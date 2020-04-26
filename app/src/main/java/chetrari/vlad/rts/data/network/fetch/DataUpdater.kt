package chetrari.vlad.rts.data.network.fetch

import chetrari.vlad.rts.base.Dispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call

abstract class DataUpdater<in Input, Response, Output> {

    @Throws(RuntimeException::class)
    protected abstract suspend fun fetch(input: Input): Response

    @Throws(RuntimeException::class)
    protected abstract suspend fun map(input: Input, response: Response): Output

    @Throws(RuntimeException::class)
    protected abstract suspend fun persist(input: Input, output: Output)

    suspend operator fun invoke(input: Input) = withContext(Dispatcher.IO) {
        val response = fetch(input)
        val output = withContext(Dispatcher.Computation) { map(input, response) }
        persist(input, output)
    }

    protected val <T> Call<T>.body: T
        get() {
            val response = execute()
            if (response.isSuccessful) return response.body()!!
            else throw Throwable(response.errorBody()?.string())
        }
}

suspend operator fun <Response, Output> DataUpdater<Unit, Response, Output>.invoke() = invoke(Unit)