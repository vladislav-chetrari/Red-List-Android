package chetrari.vlad.rts.base

import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class EventLiveData<T> : MediatorLiveData<Event<T>>(), CoroutineUpdatable {

    override var updateProcedure: (suspend () -> Unit)? = null

    override fun update(scope: CoroutineScope) {
        if (updateProcedure == null) return
        scope.launch {
            postValue(Event.Progress)
            try {
                updateProcedure!!.invoke()
            } catch (t: Throwable) {
                postValue(Event.Error(t))
            }
        }
    }

    override fun onInactive() {
        updateProcedure = null
        super.onInactive()
    }
}