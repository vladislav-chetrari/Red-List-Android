package chetrari.vlad.redlist.app.extensions

import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import chetrari.vlad.redlist.R

fun MotionLayout.waitForTransitionEnd(@IdRes idResId: Int, onComplete: MotionLayout.() -> Unit) =
    if (currentState == idResId) onComplete(this)
    else setTransitionListener(object : TransitionAdapter() {
        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            if (currentId == idResId) onComplete(this@waitForTransitionEnd)
        }
    })

fun MotionLayout.transactionToEnd(
    @IdRes startId: Int,
    @IdRes endId: Int,
    durationMillis: Int = resources.getInteger(android.R.integer.config_longAnimTime)
) {
    setTransition(R.id.step4, R.id.end)
    setTransitionDuration(durationMillis)
    transitionToEnd()
}