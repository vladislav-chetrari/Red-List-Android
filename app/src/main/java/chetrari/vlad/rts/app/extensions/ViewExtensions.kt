package chetrari.vlad.rts.app.extensions

import android.view.View
import chetrari.vlad.rts.R
import com.google.android.material.snackbar.Snackbar

fun View.errorSnackbar(
    message: String = this.resources.getString(R.string.message_error),
    retryButtonText: String = this.resources.getString(R.string.retry),
    retryAction: () -> Unit
) = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    .setAction(retryButtonText) { retryAction() }
    .show()