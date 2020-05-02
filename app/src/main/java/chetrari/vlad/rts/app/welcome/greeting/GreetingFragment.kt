package chetrari.vlad.rts.app.welcome.greeting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.toggleVisibilityAnimated
import chetrari.vlad.rts.app.extensions.waitForTransitionEnd
import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.base.BaseFragment
import chetrari.vlad.rts.base.Event
import kotlinx.android.synthetic.main.fragment_greeting.*

class GreetingFragment : BaseFragment(R.layout.fragment_greeting) {

    private val viewModel by provide<GreetingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        proceed.setOnClickListener { viewModel.onProceed() }
    }

    override fun observeLiveData() {
        viewModel.welcomePass.observe { if (it) onProceed() }
        viewModel.loadComplete.observe { isComplete ->
            if (isComplete) motionLayout.waitForTransitionEnd(R.id.step4) {
                motionLayout.run {
                    setTransition(R.id.step4, R.id.end)
                    setTransitionDuration(resources.getInteger(android.R.integer.config_longAnimTime))
                    transitionToEnd()
                }
            }
        }

        animateStep(viewModel.countries, R.string.label_loading_countries)
    }

    private fun onProceed() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun <T> animateStep(liveData: LiveData<Event<T>>, @StringRes stringResId: Int) {
        val textView = TextView(requireContext()).also {
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
}