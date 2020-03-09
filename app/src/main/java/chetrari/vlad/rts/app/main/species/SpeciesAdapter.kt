package chetrari.vlad.rts.app.main.species

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.load
import chetrari.vlad.rts.data.model.ui.Species
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_species.*

class SpeciesAdapter(
    private val onLoadImage: (Species) -> Unit
) : ListAdapter<Species, SpeciesAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_species, parent, false),
        onLoadImage
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(
        override val containerView: View,
        private val onLoadImage: (Species) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(species: Species) {
            name.text = species.scientificName
            species.images.firstOrNull()?.thumbnail?.let { thumbnail.load(it) } ?: onLoadImage(species)
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Species>() {
        override fun areItemsTheSame(oldItem: Species, newItem: Species) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Species, newItem: Species) = oldItem == newItem
                && oldItem.images.count() == newItem.images.count()
    }
}