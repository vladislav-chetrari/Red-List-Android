package chetrari.vlad.redlist.app.main.species

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.extensions.doOnDestroy
import chetrari.vlad.redlist.app.extensions.errorSnackbar
import chetrari.vlad.redlist.app.main.species.SpeciesListFragmentDirections.Companion.actionSpeciesListFragmentToSpeciesFragment
import chetrari.vlad.redlist.base.BaseFragment
import chetrari.vlad.redlist.data.persistence.model.Species
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar_default.*

class SpeciesListFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by provide<SpeciesListViewModel>()
    private val args by navArgs<SpeciesListFragmentArgs>()
    private val adapter by lazy { SpeciesListAdapter(::onSpeciesSelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().setupToolbar(toolbar)
        list.adapter = adapter
        doOnDestroy { list.adapter = null }
        refreshLayout.setOnRefreshListener(viewModel::onRefresh)
        args.country?.let(viewModel::onFilter)
        args.vulnerability?.let(viewModel::onFilter)
    }

    override fun observeLiveData() = viewModel.species.observeEvent(
        onProgress = { refreshLayout.isRefreshing = it },
        onError = { refreshLayout.errorSnackbar(retryAction = viewModel::onRefresh) },
        onSuccess = adapter::submitList
    )

    private fun onSpeciesSelected(species: Species) = findNavController()
        .navigate(actionSpeciesListFragmentToSpeciesFragment(species.id))
}