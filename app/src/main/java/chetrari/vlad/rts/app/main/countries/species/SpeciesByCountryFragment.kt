package chetrari.vlad.rts.app.main.countries.species

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.base.BaseFragment
import chetrari.vlad.rts.base.lazily
import kotlinx.android.synthetic.main.fragment_list.*

class SpeciesByCountryFragment : BaseFragment(R.layout.fragment_list) {

    private val args by navArgs<SpeciesByCountryFragmentArgs>()
    private val adapter by lazily { SpeciesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        list.adapter = adapter
        adapter.submitList(args.species.toList())
    }
}