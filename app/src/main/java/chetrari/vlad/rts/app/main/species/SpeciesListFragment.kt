package chetrari.vlad.rts.app.main.species

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_list.*

class SpeciesListFragment : BaseFragment(R.layout.fragment_list) {

    private val viewModel by provide<SpeciesViewModel>()
    private val args by navArgs<SpeciesListFragmentArgs>()
    private val adapter by lazy { SpeciesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        list.adapter = adapter
        args.country?.let(viewModel::onSearchByCountry)
    }

    override fun observeLiveData() = viewModel.species.observe(
        onProgress = { refreshLayout.isRefreshing = true },
        onComplete = { refreshLayout.isRefreshing = false },
        consumer = adapter::submitList
    )
}