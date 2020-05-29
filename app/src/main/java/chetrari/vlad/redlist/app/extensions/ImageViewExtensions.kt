package chetrari.vlad.redlist.app.extensions

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun ImageView.load(url: String?, callback: Callback? = null) = Picasso.get()
    .load(if (url.isNullOrBlank()) ERROR_URL else url)
    .resize(1440, 2560)
    .onlyScaleDown()
    .let { if (callback == null) it.into(this@load) else it.into(this@load, callback) }

private const val ERROR_URL = "http://error"