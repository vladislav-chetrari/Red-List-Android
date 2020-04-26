package chetrari.vlad.rts.app.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.load
import com.squareup.picasso.Callback
import kotlinx.android.synthetic.main.view_loading_image.view.*
import timber.log.Timber

class LoadingImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Callback {

    init {
        inflate(context, R.layout.view_loading_image, this)
    }

    var isLoading: Boolean
        get() = _progress.isVisible
        set(value) {
            _progress.isVisible = value
            _image.isVisible = !value
        }

    fun load(imageLink: String) {
        isLoading = true
        _image.load(imageLink, this)
    }

    override fun onSuccess() {
        _image.scaleType = ImageView.ScaleType.CENTER_CROP
        isLoading = false
    }

    override fun onError(e: Exception?) {
        e?.let { Timber.w(it) }
        _image.scaleType = ImageView.ScaleType.CENTER
        _image.setImageDrawable(context.getDrawable(R.drawable.ic_error_outline_accent_24dp))
        isLoading = false
    }
}