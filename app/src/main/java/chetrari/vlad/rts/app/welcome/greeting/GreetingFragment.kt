package chetrari.vlad.rts.app.welcome.greeting

import chetrari.vlad.rts.R
import chetrari.vlad.rts.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_greeting.*

class GreetingFragment : BaseFragment(R.layout.fragment_greeting) {

    private val viewModel by provide<GreetingViewModel>()

    override fun observeLiveData() = viewModel.run {
        progressSteps.observe { progressBar.max = it }
        currentStep.observe {
            progressBar.progress = it.first
            progressStep.text = it.second
        }
    }
}