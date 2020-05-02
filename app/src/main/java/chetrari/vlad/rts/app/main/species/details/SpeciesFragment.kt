package chetrari.vlad.rts.app.main.species.details

import android.graphics.Color.WHITE
import android.os.Bundle
import android.text.Spannable.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.SpannableString
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.Span
import chetrari.vlad.rts.app.extensions.errorSnackbar
import chetrari.vlad.rts.app.extensions.setNavIconColor
import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.app.main.species.details.SpeciesFragmentDirections.Companion.actionSpeciesFragmentToImageGalleryActivity
import chetrari.vlad.rts.base.BaseFragment
import chetrari.vlad.rts.data.persistence.model.Narrative
import chetrari.vlad.rts.data.persistence.model.Species
import chetrari.vlad.rts.data.persistence.type.Vulnerability
import kotlinx.android.synthetic.main.fragment_species.*
import timber.log.Timber
import javax.inject.Inject

class SpeciesFragment : BaseFragment(R.layout.fragment_species) {

    @Inject
    internal lateinit var span: Span
    private val viewModel by provide<SpeciesViewModel>()
    private val args by navArgs<SpeciesFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        toolbar.setNavIconColor(WHITE)
        collapsingToolbar.setExpandedTitleColor(WHITE)
        collapsingToolbar.setCollapsedTitleTextColor(WHITE)
        refreshLayout.setOnRefreshListener(::refresh)
    }

    override fun observeLiveData() = viewModel.run {
        onSpeciesIdReceived(args.speciesId)
        species.observeEvent(
            onError = { coordinator.errorSnackbar { refresh() };Timber.w(it) },
            onProgress = { refreshLayout.isRefreshing = true },
            onComplete = { refreshLayout.isRefreshing = false }
        ) {
            collapsingToolbar.title = it.commonName
            scientificName.value = it.scientificName
            setupVulnerability(it.category)
            setupImages(it)
            setupTaxonomy(it)
            it.narrative.target?.let(::setupNarrative)
        }
    }

    private fun refresh() = viewModel.onRefresh()

    private fun setupVulnerability(category: Vulnerability) {
        val originalString = "$category - ${getString(category.stringResId)}"
        val startIndex = 0
        val endIndex = category.toString().length
        vulnerability.setValue(SpannableString(originalString).apply {
            setSpan(span.foreground(category.colorResId), startIndex, endIndex, SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(span.bold, startIndex, endIndex, SPAN_INCLUSIVE_EXCLUSIVE)
        })
    }

    private fun setupImages(species: Species) {
        val images = species.images
        if (images.isNotEmpty()) toolbarImage.run {
            load(images[0].fullSize)
            setOnClickListener {
                findNavController().navigate(
                    actionSpeciesFragmentToImageGalleryActivity(
                        images.toTypedArray(),
                        species.commonName
                    )
                )
            }
        }
    }

    private fun setupNarrative(narrative: Narrative) = listOf(
        taxonomicNotes to narrative.taxonomicNotes,
        justification to narrative.justification,
        geographicRange to narrative.geographicRange,
        population to narrative.population,
        habitatAndEcology to narrative.habitatAndEcology,
        threats to narrative.threats,
        conservationActions to narrative.conservationActions
    ).forEach {
        it.first.content = it.second
    }

    private fun setupTaxonomy(species: Species) {
        taxonomy.data = with(species) {
            listOf(
                R.string.label_kingdom to kingdom,
                R.string.label_phylum to phylum,
                R.string.label_class to bioClass,
                R.string.label_order to order,
                R.string.label_family to family,
                R.string.label_genus to genus
            )
        }.map { getString(it.first) to it.second }
    }
}