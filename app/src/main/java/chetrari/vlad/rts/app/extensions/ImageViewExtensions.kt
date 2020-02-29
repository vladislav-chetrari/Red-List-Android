package chetrari.vlad.rts.app.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.load(url: String?) = Picasso.get().load(url ?: ERROR_URL).into(this)

private const val ERROR_URL = "http://error"