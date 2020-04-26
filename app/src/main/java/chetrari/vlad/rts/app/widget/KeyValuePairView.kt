package chetrari.vlad.rts.app.widget

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.setAppearance
import kotlinx.android.synthetic.main.view_key_value_pair.view.*

//TODO fix behavior, it actions negatively to another views in same parent
class KeyValuePairView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_key_value_pair, this)
        setupStyledAttributes(attrs)
    }

    var hideWhenValueEmpty: Boolean = true
        set(value) {
            field = value
            _container.isVisible = !(value && this.value.isBlank())
        }
    var key: String
        get() = _key.text.toString()
        set(value) {
            _key.text = value
            hideWhenValueEmpty = hideWhenValueEmpty
        }
    var value: String
        get() = _value.text.toString()
        set(value) {
            _value.text = value
            hideWhenValueEmpty = hideWhenValueEmpty
        }
    var isEnlarged: Boolean = false
        set(value) {
            field = value
            if (value) {
                _key.setAppearance(android.R.style.TextAppearance_Material_Subhead)
                _key.setTypeface(_key.typeface, Typeface.BOLD)
                _value.setAppearance(android.R.style.TextAppearance_Material_Subhead)
            } else {
                _key.setAppearance(android.R.style.TextAppearance_Material_Body2)
                _value.setAppearance(android.R.style.TextAppearance_Material_Body1)
            }
        }

    fun setValue(spannable: Spannable) {
        _value.text = spannable
        hideWhenValueEmpty = hideWhenValueEmpty
    }

    private fun setupStyledAttributes(
        attrs: AttributeSet?
    ) = context.obtainStyledAttributes(attrs, R.styleable.KeyValuePairView).run {
        key = getString(R.styleable.KeyValuePairView_keyText) ?: ""
        value = getString(R.styleable.KeyValuePairView_valueText) ?: ""
        hideWhenValueEmpty = getBoolean(R.styleable.KeyValuePairView_hideWhenValueEmpty, true)
        isEnlarged = getBoolean(R.styleable.KeyValuePairView_enlarged, false)
        recycle()
    }
}