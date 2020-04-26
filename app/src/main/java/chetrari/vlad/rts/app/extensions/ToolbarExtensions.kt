package chetrari.vlad.rts.app.extensions

import androidx.annotation.ColorInt
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar

fun Toolbar.setNavIconColor(@ColorInt colorInt: Int) {
    (navigationIcon as? DrawerArrowDrawable)?.color = colorInt
}