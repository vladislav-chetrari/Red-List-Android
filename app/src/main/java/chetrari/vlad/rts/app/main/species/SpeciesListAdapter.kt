package chetrari.vlad.rts.app.main.species

import android.content.res.ColorStateList
import android.text.Spannable.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.Span
import chetrari.vlad.rts.data.persistence.model.Species
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_species.*

class SpeciesListAdapter(
    private val onSpeciesSelected: (Species) -> Unit
) : PagedListAdapter<Species, SpeciesListAdapter.ViewHolder>(ItemCallback()) {

    var highlightedText = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_species, parent, false),
        onSpeciesSelected
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), highlightedText)

    class ViewHolder(
        override val containerView: View,
        private val onSpeciesSelected: (Species) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val span = Span(containerView.resources)

        fun bind(species: Species?, highlightedText: String) {
            species ?: return
            scientificName.text = SpannableString(species.scientificName).apply {
                if (contains(highlightedText, true)) {
                    val startIndex = indexOf(highlightedText, ignoreCase = true)
                    val endIndex = startIndex + highlightedText.length
                    setSpan(span.background(R.color.yellow400), startIndex, endIndex, SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }
            species.category.let {
                val color = ResourcesCompat.getColor(containerView.resources, it.colorResId, null)
                vulnerability.backgroundTintList = ColorStateList.valueOf(color)
                vulnerability.text = "$it"
            }
            containerView.setOnClickListener { onSpeciesSelected(species) }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Species>() {
        override fun areItemsTheSame(oldItem: Species, newItem: Species) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Species, newItem: Species) = oldItem == newItem
    }
}