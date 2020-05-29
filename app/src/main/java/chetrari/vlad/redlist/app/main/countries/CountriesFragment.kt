package chetrari.vlad.redlist.app.main.countries

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.extensions.doOnDestroy
import chetrari.vlad.redlist.app.extensions.errorSnackbar
import chetrari.vlad.redlist.app.main.countries.CountriesFragmentDirections.Companion.actionDestinationCountriesToSpeciesListFragment
import chetrari.vlad.redlist.base.BaseFragment
import chetrari.vlad.redlist.data.persistence.model.Country
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar_default.*

class CountriesFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by provide<CountriesViewModel>()
    private val adapter by lazy { CountriesAdapter(::onCountrySelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().setupToolbar(toolbar)
        list.adapter = adapter
        doOnDestroy { list.adapter = null }
        refreshLayout.setOnRefreshListener(viewModel::onRefresh)
    }

    override fun observeLiveData() = viewModel.countries.observeEvent(
        onProgress = { refreshLayout.isRefreshing = it },
        onError = { container.errorSnackbar(retryAction = viewModel::onRefresh) },
        onSuccess = adapter::submitList
    )

    private fun onCountrySelected(country: Country) = findNavController()
        .navigate(actionDestinationCountriesToSpeciesListFragment(country))
}