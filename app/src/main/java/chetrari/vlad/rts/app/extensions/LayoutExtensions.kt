package chetrari.vlad.rts.app.extensions

import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter

fun MotionLayout.waitForTransitionEnd(@IdRes idResId: Int, onComplete: () -> Unit) =
    if (currentState == idResId) onComplete()
    else setTransitionListener(object : TransitionAdapter() {
        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            if (currentId == idResId) onComplete()
        }
    })