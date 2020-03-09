package chetrari.vlad.rts.app.main.countries

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.errorSnackbar
import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.app.main.countries.CountriesFragmentDirections.Companion.actionDestinationCountriesToSpeciesListFragment
import chetrari.vlad.rts.base.BaseFragment
import chetrari.vlad.rts.data.model.ui.Country
import kotlinx.android.synthetic.main.fragment_list.*

class CountriesFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by provide<CountriesViewModel>()
    private val adapter by lazy { CountriesAdapter(::onCountrySelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        list.adapter = adapter
        refreshLayout.setOnRefreshListener(viewModel::onRefresh)
    }

    override fun observeLiveData() = viewModel.countries.observe(
        onProgress = { refreshLayout.isRefreshing = true },
        onComplete = { refreshLayout.isRefreshing = false },
        onError = { container.errorSnackbar(retryAction = viewModel::onRefresh) },
        onSuccess = adapter::submitList
    )

    override fun onDestroyView() {
        list.adapter = adapter
        super.onDestroyView()
    }

    private fun onCountrySelected(country: Country) =
        findNavController().navigate(actionDestinationCountriesToSpeciesListFragment(country))
}