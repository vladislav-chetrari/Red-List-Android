package chetrari.vlad.rts.hello

import androidx.lifecycle.viewModelScope
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.base.invoke
import chetrari.vlad.rts.usecase.Greeter
import javax.inject.Inject

class HelloViewModel @Inject constructor(greeter: Greeter) : BaseViewModel() {

    val greeting = greeter(viewModelScope)
}