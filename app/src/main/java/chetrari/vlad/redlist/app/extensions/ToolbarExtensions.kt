package chetrari.vlad.redlist.app.extensions

import android.graphics.drawable.VectorDrawable
import androidx.annotation.ColorInt
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar

fun Toolbar.setNavIconColor(@ColorInt colorInt: Int) {
    (navigationIcon as? DrawerArrowDrawable)?.color = colorInt
    (navigationIcon as? VectorDrawable)?.setTint(colorInt)
}