package chetrari.vlad.rts.app.extensions

import android.os.Build
import android.widget.TextView
import androidx.annotation.StyleRes

@Suppress("DEPRECATION")
fun TextView.setAppearance(@StyleRes styleResId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(styleResId)
    else setTextAppearance(context, styleResId)
}