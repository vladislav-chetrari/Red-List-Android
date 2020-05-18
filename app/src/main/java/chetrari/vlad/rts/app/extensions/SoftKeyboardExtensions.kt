package chetrari.vlad.rts.app.extensions

import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

fun AppCompatActivity.hideKeyboard() {
    val service = getSystemService<InputMethodManager>()
    val currentFocusedView = currentFocus
    if (service != null && currentFocusedView != null) {
        service.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
    }
}

fun Fragment.hideKeyboard() {
    (requireActivity() as? AppCompatActivity)?.hideKeyboard()
}