package chetrari.vlad.rts.app

import android.content.res.Resources
import android.graphics.Typeface
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat.getColor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Span @Inject constructor(private val resources: Resources) {

    val bold: StyleSpan
        get() = StyleSpan(Typeface.BOLD)

    fun foreground(@ColorRes colorResId: Int) = ForegroundColorSpan(getColor(resources, colorResId, null))

    fun background(@ColorRes colorResId: Int) = BackgroundColorSpan(getColor(resources, colorResId, null))

    fun relativeSize(multiplier: Float) = RelativeSizeSpan(multiplier)
}