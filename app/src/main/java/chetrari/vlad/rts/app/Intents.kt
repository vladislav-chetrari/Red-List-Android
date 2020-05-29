package chetrari.vlad.rts.app

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File

object Intents {

    fun shareText(text: String) = Intent().apply {
        action = ACTION_SEND
        type = "text/plain"
        putExtra(EXTRA_TEXT, text)
    }

    //FIXME (without permissions, save to images or get uri from picasso somehow)
    fun shareImageUrl(
        context: Context,
        url: String,
        onError: (Throwable?) -> Unit = {},
        onIntentReady: (Intent) -> Unit
    ) {
        if (url.isBlank()) {
            onError(Throwable("Empty url"))
            return
        }
        Picasso.get().load(url).into(object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) = Unit
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) = onError(e)
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                if (bitmap == null) {
                    onError(Throwable())
                    return
                }
                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.pathSeparator + "tmp")
                        put(MediaStore.MediaColumns.IS_PENDING, 1)
                    }
                })
                if (uri == null) {
                    onError(Throwable())
                    return
                }
                val intent = Intent(ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(EXTRA_STREAM, uri)
                }
                onIntentReady(intent)
            }
        })
    }

    fun webUrl(url: String) = Intent(ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
}