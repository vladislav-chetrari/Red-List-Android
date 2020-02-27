package chetrari.vlad.rts.app.main.countries

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.app.main.countries.CountriesFragmentDirections.Companion.actionDestinationCountriesToSpeciesByCountryFragment
import chetrari.vlad.rts.base.BaseFragment
import chetrari.vlad.rts.base.lazily
import kotlinx.android.synthetic.main.fragment_list.*

class CountriesFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by provide<CountriesViewModel>()
    private val adapter by lazily { CountriesAdapter(viewModel::onCountrySelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        list.adapter = adapter
        //TODO setup refreshLayout
    }

    override fun observeLiveData() = viewModel.run {
        countries.observe(
            onProgress = { progressBar.isVisible = true }
            //TODO event snackbar with button retry
        ) {
            adapter.submitList(it)
            progressBar.isVisible = false
        }
        countryInProgress.observe { adapter.countryInProgress = it }
        speciesByCountry.observe(onError = {
            adapter.countryInProgress = null
            //TODO event snackbar with button retry
        }) {
            adapter.countryInProgress = null
            findNavController().navigate(actionDestinationCountriesToSpeciesByCountryFragment(it.toTypedArray()))
        }
    }

    override fun onDestroyView() {
        list.adapter = adapter
        super.onDestroyView()
    }
}