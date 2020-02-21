package chetrari.vlad.rts.hello

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.base.map
import chetrari.vlad.rts.usecase.Greeter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class HelloViewModel @Inject constructor(greeter: Greeter) : BaseViewModel() {

    val greeting = greeter(viewModelScope, Nothing)

    //TODO to create another useCase which does this mapping
    @SuppressLint("DefaultLocale")
    private val greetObserver: Observer<Event<String>> = Observer {
        if (it is Event.Success) viewModelScope.launch {
            delay(2000L)
            greeter(viewModelScope, if (it.result.contains(ALICE)) "Vlad" else ALICE)
        }
    }

    init {
        greeting.observeForever(greetObserver)
    }

    override fun onCleared() {
        greeting.removeObserver(greetObserver)
        super.onCleared()
    }

    private companion object {
        const val ALICE = "Alice"
    }
}