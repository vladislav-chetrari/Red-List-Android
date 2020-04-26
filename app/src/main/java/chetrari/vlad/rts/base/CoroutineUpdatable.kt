package chetrari.vlad.rts.base

import kotlinx.coroutines.CoroutineScope

interface CoroutineUpdatable {

    var updateProcedure: (suspend () -> Unit)?

    fun update(scope: CoroutineScope)
}