package chetrari.vlad.rts.app.main.countries

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import chetrari.vlad.rts.R
import chetrari.vlad.rts.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_list.*

class CountriesFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by provide<CountriesViewModel>()
    private val adapter = CountriesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO setup refreshLayout
        list.adapter = adapter
    }

    override fun observeLiveData() = viewModel.run {
        countries.observe(
            onProgress = { progressBar.isVisible = true }
            //TODO event snackbar with button retry
        ) {
            adapter.submitList(it)
            progressBar.isVisible = false
        }
    }
}