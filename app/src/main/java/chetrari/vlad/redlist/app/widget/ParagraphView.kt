package chetrari.vlad.redlist.app.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.core.view.isVisible
import chetrari.vlad.redlist.R
import kotlinx.android.synthetic.main.view_paragraph.view.*

class ParagraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    private var interactionsEnabled: Boolean
        get() = _motionLayout.isInteractionEnabled
        set(value) {
            _motionLayout.isInteractionEnabled = value
            _expandIcon.isVisible = value
        }
    var title: String
        get() = _title.text.toString()
        set(value) {
            _title.text = value
        }
    var content: String = ""
        set(value) {
            field = value
            interactionsEnabled = value.isNotBlank()
            _content.text = fromHtml(value, FROM_HTML_MODE_LEGACY).trim()
        }
    var isExpanded: Boolean
        get() = _motionLayout.currentState == R.id.expanded
        set(value) = _motionLayout.run {
            if (value) transitionToEnd() else transitionToStart()
        }

    init {
        inflate(context, R.layout.view_paragraph, this)
        setupStyledAttributes(attrs)
        interactionsEnabled = false
    }

    private fun setupStyledAttributes(
        attrs: AttributeSet?
    ) = context.obtainStyledAttributes(attrs, R.styleable.ParagraphView).run {
        title = getString(R.styleable.ParagraphView_titleText) ?: ""
        content = getString(R.styleable.ParagraphView_contentText) ?: ""
        recycle()
    }
}