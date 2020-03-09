package chetrari.vlad.rts.app.main.species

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.errorSnackbar
import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.base.BaseFragment
import chetrari.vlad.rts.data.model.ui.Species
import kotlinx.android.synthetic.main.fragment_species_list.*

class SpeciesListFragment : BaseFragment(R.layout.fragment_species_list) {

    private val viewModel by provide<SpeciesViewModel>()
    private val args by navArgs<SpeciesListFragmentArgs>()
    private val adapter by lazy { SpeciesAdapter(::onUpdateImage) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        list.adapter = adapter
        refreshLayout.setOnRefreshListener(::onRefresh)
    }

    override fun observeLiveData() = onRefresh()

    private fun onRefresh() {
        args.country?.let(viewModel::speciesByCountry)?.observe(
            onProgress = { refreshLayout.isRefreshing = true },
            onComplete = { refreshLayout.isRefreshing = false },
            onError = { refreshLayout.errorSnackbar(retryAction = this::onRefresh) },
            onSuccess = adapter::submitList
        )
    }

    private fun onUpdateImage(species: Species) = viewModel.onUpdateImage(species)
}