package chetrari.vlad.rts.data.domain

import chetrari.vlad.rts.base.Dispatcher
import chetrari.vlad.rts.base.UseCase
import retrofit2.Call

abstract class IOUseCase<in Input, Output> : UseCase<Input, Output>() {

    final override val dispatcher = Dispatcher.IO

    protected val <T> Call<T>.body: T
        get() {
            val response = execute()
            if (response.isSuccessful) return response.body()!!
            else throw Throwable(response.errorBody()?.string())
        }
}