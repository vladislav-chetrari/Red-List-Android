package chetrari.vlad.rts.app.main.countries

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.errorSnackbar
import chetrari.vlad.rts.app.main.countries.CountriesFragmentDirections.Companion.actionDestinationCountriesToSpeciesListFragment
import chetrari.vlad.rts.base.BaseFragment
import chetrari.vlad.rts.data.persistence.model.Country
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar_default.*

class CountriesFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by provide<CountriesViewModel>()
    private val adapter by lazy { CountriesAdapter(::onCountrySelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupWithNavController(findNavController())
        list.adapter = adapter
        refreshLayout.setOnRefreshListener(viewModel::onRefresh)
    }

    override fun observeLiveData() = viewModel.countries.observeEvent(
        onProgress = { refreshLayout.isRefreshing = it },
        onError = { container.errorSnackbar(retryAction = viewModel::onRefresh) },
        onSuccess = adapter::submitList
    )

    override fun onDestroyView() {
        list.adapter = null
        super.onDestroyView()
    }

    private fun onCountrySelected(country: Country) = findNavController()
        .navigate(actionDestinationCountriesToSpeciesListFragment(country))
}