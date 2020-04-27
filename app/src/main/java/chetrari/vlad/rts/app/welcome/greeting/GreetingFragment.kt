package chetrari.vlad.rts.app.welcome.greeting

import chetrari.vlad.rts.R
import chetrari.vlad.rts.base.BaseFragment

//TODO fix animation: view paths
class GreetingFragment : BaseFragment(R.layout.fragment_greeting) {

    private val viewModel by provide<GreetingViewModel>()

    override fun observeLiveData() {

    }
}