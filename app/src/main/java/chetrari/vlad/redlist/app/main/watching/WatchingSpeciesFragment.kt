package chetrari.vlad.redlist.app.main.watching

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.extensions.doOnDestroy
import chetrari.vlad.redlist.app.main.watching.WatchingSpeciesFragmentDirections.Companion.actionWatchingSpeciesFragmentToSpeciesFragment
import chetrari.vlad.redlist.base.BaseFragment
import chetrari.vlad.redlist.data.persistence.model.Species
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar_default.*

class WatchingSpeciesFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by provide<WatchingViewModel>()
    private val adapter = WatchingSpeciesListAdapter(::onSpeciesSelected)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().setupToolbar(toolbar)
        list.adapter = adapter
        list.layoutManager = GridLayoutManager(requireContext(), 2)
        doOnDestroy { list.adapter = null }
        refreshLayout.setOnRefreshListener(viewModel::onRefresh)
    }

    override fun observeLiveData() = viewModel.species.observeEvent(
        onProgress = { refreshLayout.isRefreshing = it },
        onSuccess = adapter::submitList
    )

    private fun onSpeciesSelected(species: Species) = findNavController()
        .navigate(actionWatchingSpeciesFragmentToSpeciesFragment(species.id))
}