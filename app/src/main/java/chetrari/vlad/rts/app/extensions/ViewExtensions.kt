package chetrari.vlad.rts.app.extensions

import android.view.MotionEvent
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.LinearInterpolator
import chetrari.vlad.rts.R
import com.google.android.material.snackbar.Snackbar

//used then view click listener is overridden by motion layout
//FIXME behavior: then clicked and moved finger to another place, it also triggers
fun View.setOnActionUpListener(action: () -> Unit) = setOnTouchListener { _, event ->
    if (event.action == MotionEvent.ACTION_UP) action()
    false
}

fun View.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionButtonTextActionPair: Pair<String, () -> Unit>? = null
) = Snackbar.make(this, message, duration).apply {
    if (actionButtonTextActionPair != null) setAction(actionButtonTextActionPair.first) {
        actionButtonTextActionPair.second()
    }
}.show()

fun View.errorSnackbar(
    message: String = this.resources.getString(R.string.message_error),
    retryButtonText: String = this.resources.getString(R.string.retry),
    retryAction: () -> Unit
) = snackbar(message, Snackbar.LENGTH_LONG, retryButtonText to retryAction)

fun View.rotateAnimated(
    animationTimeMillis: Int = resources.getInteger(android.R.integer.config_shortAnimTime),
    degrees: Float,
    onAnimationEnd: () -> Unit = {}
) = ifNotAnimating {
    animate().rotationBy(degrees).withProperties(this, animationTimeMillis, onAnimationEnd = onAnimationEnd).start()
}

fun View.toggleVisibilityAnimated(
    animationTimeMillis: Int = resources.getInteger(android.R.integer.config_shortAnimTime),
    onAnimationEnd: () -> Unit = {}
) = ifNotAnimating {
    animate().alpha(if (alpha == 0f) 1f else 0f)
        .withProperties(this, animationTimeMillis, onAnimationEnd = onAnimationEnd)
        .start()
}

private fun View.ifNotAnimating(action: () -> Unit) {
    if ((getTag(R.id.TAG_IS_ANIMATING) as? Boolean) == true) return
    action()
}

private fun ViewPropertyAnimator.withProperties(
    view: View,
    animationTimeMillis: Int,
    onAnimationStart: () -> Unit = {},
    onAnimationEnd: () -> Unit = {}
) = withStartAction { view.setTag(R.id.TAG_IS_ANIMATING, true); onAnimationStart() }
    .withEndAction { view.setTag(R.id.TAG_IS_ANIMATING, false); onAnimationEnd() }
    .setDuration(animationTimeMillis.toLong())
    .setInterpolator(LinearInterpolator())