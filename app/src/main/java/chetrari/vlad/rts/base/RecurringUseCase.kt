package chetrari.vlad.rts.base

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class RecurringUseCase<Input, Output> {
    protected open val dispatcher: CoroutineDispatcher = Dispatchers.Main

    private val liveData = MutableLiveData<Event<Output>>()


}