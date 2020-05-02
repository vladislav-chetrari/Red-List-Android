package chetrari.vlad.rts.app.main.species

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.errorSnackbar
import chetrari.vlad.rts.app.extensions.setNavIconColor
import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.app.main.species.SpeciesListFragmentDirections.Companion.actionSpeciesListFragmentToSpeciesFragment
import chetrari.vlad.rts.base.BaseFragment
import chetrari.vlad.rts.data.persistence.model.Species
import kotlinx.android.synthetic.main.fragment_species_list.*
import kotlinx.android.synthetic.main.toolbar_simple.*

class SpeciesListFragment : BaseFragment(R.layout.fragment_species_list) {

    private val viewModel by provide<SpeciesListViewModel>()
    private val args by navArgs<SpeciesListFragmentArgs>()
    private val adapter by lazy { SpeciesListAdapter(::onSpeciesSelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        toolbar.setNavIconColor(Color.WHITE)
        list.adapter = adapter
        refreshLayout.setOnRefreshListener(viewModel::onRefresh)
    }

    override fun observeLiveData() {
        args.country?.let { country ->
            viewModel.onSearchByCountry(country)
            viewModel.species.observeEvent(
                onProgress = { refreshLayout.isRefreshing = true },
                onComplete = { refreshLayout.isRefreshing = false },
                onError = { refreshLayout.errorSnackbar(retryAction = viewModel::onRefresh) },
                onSuccess = adapter::submitList
            )
        }
    }

    override fun onDestroyView() {
        list.adapter = null
        super.onDestroyView()
    }

    private fun onSpeciesSelected(species: Species) = findNavController()
        .navigate(actionSpeciesListFragmentToSpeciesFragment(species.id))
}