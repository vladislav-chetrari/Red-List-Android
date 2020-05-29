package chetrari.vlad.redlist.app.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import chetrari.vlad.redlist.R
import kotlinx.android.synthetic.main.view_key_value_pairs_paragraph.view.*

class KeyValuePairsParagraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    var title: String = ""
        set(value) {
            field = value
            _title.text = value
        }
    var data: List<Pair<String, String>> = emptyList()
        set(value) {
            field = value
            _content.removeAllViews()
            value.map { pair ->
                KeyValuePairView(context).also {
                    it.key = pair.first
                    it.value = pair.second
                }
            }.forEach { _content.addView(it) }
        }
    var isExpanded: Boolean
        get() = _motionLayout.currentState == R.id.expanded
        set(value) = _motionLayout.run {
            if (value) transitionToEnd() else transitionToStart()
        }

    init {
        inflate(context, R.layout.view_key_value_pairs_paragraph, this)
        setupStyledAttributes(attrs)
    }

    private fun setupStyledAttributes(
        attrs: AttributeSet?
    ) = context.obtainStyledAttributes(attrs, R.styleable.KeyValuePairsParagraphView).run {
        title = getString(R.styleable.KeyValuePairsParagraphView_title) ?: ""
        recycle()
    }
}