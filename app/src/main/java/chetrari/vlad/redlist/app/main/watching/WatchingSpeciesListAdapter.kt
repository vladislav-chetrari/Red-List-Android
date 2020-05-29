package chetrari.vlad.redlist.app.main.watching

import android.content.res.Resources
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.R.string.pattern_hyphenated_text_pair
import chetrari.vlad.redlist.app.Span
import chetrari.vlad.redlist.app.extensions.load
import chetrari.vlad.redlist.data.persistence.model.Species
import chetrari.vlad.redlist.data.persistence.type.Vulnerability
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_watching_species.*

class WatchingSpeciesListAdapter(
    private val onSpeciesSelected: (Species) -> Unit
) : ListAdapter<Species, WatchingSpeciesListAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_watching_species, parent, false),
        onSpeciesSelected
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(
        override val containerView: View,
        private val onSpeciesSelected: (Species) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val span = Span(res)
        private val res: Resources
            get() = containerView.resources

        fun bind(species: Species) {
            commonName.text = species.commonName
            commonName.marquee()
            scientificName.text = species.scientificName
            scientificName.marquee()
            vulnerability.text = vulnerabilityText(species.category)
            species.images.firstOrNull()?.thumbnail?.let {
                image.load(it)
            }
            containerView.setOnClickListener { onSpeciesSelected(species) }
        }

        private fun vulnerabilityText(vulnerability: Vulnerability): SpannableString {
            val text = vulnerability.let {
                res.getString(pattern_hyphenated_text_pair, "$it", res.getString(it.stringResId))
            }
            return SpannableString(text).apply {
                setSpan(span.foreground(vulnerability.colorResId), 0, text.length, SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }

        private fun TextView.marquee() {
            ellipsize = TextUtils.TruncateAt.MARQUEE
            isSingleLine = true
            marqueeRepeatLimit = 2
            isSelected = true
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Species>() {
        override fun areItemsTheSame(oldItem: Species, newItem: Species) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Species, newItem: Species) = oldItem == newItem
    }
}
