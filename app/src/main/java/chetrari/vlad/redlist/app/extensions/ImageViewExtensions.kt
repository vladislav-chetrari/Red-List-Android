package chetrari.vlad.redlist.app.extensions

import android.widget.ImageView
import coil.api.load
import coil.size.ViewSizeResolver

fun ImageView.load(url: String?) = load(if (url.isNullOrBlank()) ERROR_URL else url) {
    crossfade(true)
    size(ViewSizeResolver(this@load))
}

private const val ERROR_URL = "http://error"