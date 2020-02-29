package chetrari.vlad.rts.base

import kotlinx.coroutines.Dispatchers

object Dispatcher {
    val Main = Dispatchers.Main
    val IO = Dispatchers.IO
    val Computation = Dispatchers.Default
}