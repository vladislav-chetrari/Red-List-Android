package chetrari.vlad.redlist.app.main.search

import android.R.layout.select_dialog_item
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import androidx.navigation.fragment.findNavController
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.extensions.*
import chetrari.vlad.redlist.app.main.search.SpeciesSearchFragmentDirections.Companion.actionDestinationSearchToSpeciesFragment
import chetrari.vlad.redlist.app.main.species.SpeciesListAdapter
import chetrari.vlad.redlist.base.BaseFragment
import chetrari.vlad.redlist.data.persistence.model.Species
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_species_search.*
import kotlinx.android.synthetic.main.layout_search_box.*
import kotlinx.android.synthetic.main.toolbar_default.*

class SpeciesSearchFragment : BaseFragment(R.layout.fragment_species_search) {

    private val viewModel by provide<SpeciesSearchViewModel>()
    private val speciesAdapter by lazy { SpeciesListAdapter(::onSpeciesSelected) }
    private val countriesAdapter by lazy { ArrayAdapter<String>(requireContext(), select_dialog_item) }
    private val vulnerabilityAdapter by lazy { ArrayAdapter<String>(requireContext(), select_dialog_item) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().setupToolbar(toolbar)
        list.adapter = speciesAdapter
        refreshLayout.setOnRefreshListener(viewModel::onRefresh)
        doOnDestroy { list.adapter = null }
        setupFilter(addCountry, countriesAdapter, countriesChipGroup)
        setupFilter(addVulnerability, vulnerabilityAdapter, vulnerabilityChipGroup) { it.substringBefore(" - ") }
        searchButton.setOnActionUpListener { onBackPressedCallback.isEnabled = true }
        cancel.setOnActionUpListener { onCancelSearchPressed(true) }
        confirm.setOnActionUpListener(::onConfirmSearchPressed)
    }

    override fun observeLiveData() = viewModel.run {
        countryNames.observe { arrayAdapterObserver(countriesAdapter, it) }
        vulnerabilityTypes.observe { arrayAdapterObserver(vulnerabilityAdapter, it) { v -> "$v - ${getString(v.stringResId)}" } }
        species.observeEvent(
            onProgress = { refreshLayout.isRefreshing = it },
            onError = { refreshLayout.errorSnackbar(retryAction = viewModel::onRefresh) },
            onSuccess = speciesAdapter::submitList
        )
    }

    override fun handleOnBackPressed() {
        cancel.performClick()
        onCancelSearchPressed(false)
    }

    private fun setupFilter(
        autoCompleteTextView: AutoCompleteTextView,
        adapter: ArrayAdapter<String>,
        selectedItemsChipGroup: ChipGroup,
        selectedItemMapper: (String) -> String = { it }
    ) = autoCompleteTextView.run {
        setAdapter(adapter)
        threshold = 0
        setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)!!
            selectedItemsChipGroup += chip(selectedItemMapper(selectedItem)) {
                adapter.add(selectedItem)
                selectedItemsChipGroup -= it
            }
            adapter.remove(selectedItem)
            setText("")
        }
        doOnDestroy { setAdapter(null) }
    }

    private fun chip(text: String, onClose: (Chip) -> Unit) = Chip(requireContext()).apply {
        this.text = text
        chipBackgroundColor = ColorStateList.valueOf(Color.WHITE)
        isCloseIconVisible = true
        setOnCloseIconClickListener { onClose(this) }
    }

    private fun <T> arrayAdapterObserver(adapter: ArrayAdapter<String>, data: List<T>, dataMapper: (T) -> String = { "$it" }) {
        adapter.clear()
        adapter.addAll(data.map(dataMapper))
    }

    private fun onSearchAction() {
        hideKeyboard()
        onBackPressedCallback.isEnabled = false
    }

    private fun onCancelSearchPressed(clearFilters: Boolean) {
        if (clearFilters) (countriesChipGroup.chips + vulnerabilityChipGroup.chips).forEach { it.performCloseIconClick() }
        onSearchAction()
    }

    private fun onConfirmSearchPressed() {
        val nameQuery = name.text.toString()
        viewModel.onSearch(nameQuery, countriesChipGroup.labels, vulnerabilityChipGroup.labels)
        onSearchAction()
        speciesAdapter.highlightedText = nameQuery
    }

    private fun onSpeciesSelected(species: Species) = findNavController()
        .navigate(actionDestinationSearchToSpeciesFragment(species.id))

    private val ChipGroup.chips: List<Chip>
        get() = children.filterIsInstance<Chip>()

    private val ChipGroup.labels: List<String>
        get() = chips.map { it.text.toString() }
}