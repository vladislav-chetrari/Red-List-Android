package chetrari.vlad.redlist.app.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.extensions.toggleVisibilityAnimated
import chetrari.vlad.redlist.app.extensions.transactionToEnd
import chetrari.vlad.redlist.app.extensions.waitForTransitionEnd
import chetrari.vlad.redlist.app.main.MainActivity
import chetrari.vlad.redlist.base.BaseActivity
import chetrari.vlad.redlist.base.Event
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity(R.layout.activity_welcome) {

    private val viewModel by provide<WelcomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        proceed.setOnClickListener { viewModel.onProceed() }
    }

    override fun observeLiveData() = viewModel.run {
        welcomePass.observe { if (it) proceed() }
        loadComplete.observe { isComplete ->
            if (isComplete) motionLayout.waitForTransitionEnd(R.id.step4) {
                transactionToEnd(R.id.step4, R.id.end)
            }
        }
        animateStep(countries, R.string.loading_countries)
    }

    private fun <T> animateStep(liveData: LiveData<Event<T>>, @StringRes stringResId: Int) {
        val textView = TextView(this).also {
            it.text = getString(stringResId)
            it.isVisible = false
        }
        progressContainer.addView(textView, 0)
        textView.toggleVisibilityAnimated()
        liveData.observeEvent {
            textView.toggleVisibilityAnimated {
                progressContainer.removeView(textView)
            }
        }
    }

    private fun proceed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}