package chetrari.vlad.redlist.app.main.species.details

import android.graphics.Color.WHITE
import android.os.Bundle
import android.text.Spannable.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.SpannableString
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.Intents
import chetrari.vlad.redlist.app.Span
import chetrari.vlad.redlist.app.extensions.errorSnackbar
import chetrari.vlad.redlist.app.extensions.load
import chetrari.vlad.redlist.app.main.species.details.SpeciesFragmentDirections.Companion.actionSpeciesFragmentToImageGalleryActivity
import chetrari.vlad.redlist.base.BaseFragment
import chetrari.vlad.redlist.data.persistence.model.Narrative
import chetrari.vlad.redlist.data.persistence.model.Species
import chetrari.vlad.redlist.data.persistence.model.SpeciesImage
import chetrari.vlad.redlist.data.persistence.type.Vulnerability
import kotlinx.android.synthetic.main.fragment_species.*
import javax.inject.Inject

class SpeciesFragment : BaseFragment(R.layout.fragment_species) {

    @Inject
    internal lateinit var span: Span
    private val viewModel by provide<SpeciesViewModel>()
    private val args by navArgs<SpeciesFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().setupToolbar(toolbar, WHITE)
        collapsingToolbar.setExpandedTitleColor(WHITE)
        collapsingToolbar.setCollapsedTitleTextColor(WHITE)
        refreshLayout.setOnRefreshListener(::refresh)
        watch.setOnClickListener { viewModel.onWatch() }
    }

    override fun observeLiveData() = viewModel.run {
        onSpeciesIdReceived(args.speciesId)
        species.observeEvent(
            onError = ::onError,
            onProgress = { refreshLayout.isRefreshing = it }
        ) {
            collapsingToolbar.title = it.commonName
            scientificName.value = it.scientificName
            setupVulnerability(it.category)
            setupImages(it)
            setupTaxonomy(it)
            setupWatching(it)
            setupWebLink(it)
            it.narrative.target?.let(::setupNarrative)
        }
    }

    private fun setupWebLink(species: Species) = webLink.run {
        val link = species.webLink
        isVisible = link.isNotBlank()
        setOnClickListener { startActivity(Intents.webUrl(link)) }
    }

    private fun onError(throwable: Throwable) = coordinator.errorSnackbar(
        message = throwable.message ?: getString(R.string.message_error),
        retryAction = ::refresh
    )

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
            setOnClickListener { openGallery(species.commonName, images) }
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
                R.string.kingdom to kingdom,
                R.string.phylum to phylum,
                R.string.label_class to bioClass,
                R.string.order to order,
                R.string.family to family,
                R.string.genus to genus
            )
        }.map { getString(it.first) to it.second }
    }

    private fun setupWatching(species: Species) {
        val (stringRes, drawableRes) = when {
            species.watching -> R.string.unwatch to R.drawable.ic_visibility_off_white_24dp
            else -> R.string.watch to R.drawable.ic_visibility_on_white_24dp
        }
        watch.text = getString(stringRes)
        watch.icon = resources.getDrawable(drawableRes, null)
        watch.isVisible = true
    }

    private fun openGallery(title: String, images: List<SpeciesImage>) = findNavController()
        .navigate(actionSpeciesFragmentToImageGalleryActivity(images.toTypedArray(), title))
}