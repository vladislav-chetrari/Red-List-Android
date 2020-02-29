package chetrari.vlad.rts.app.main.species

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import chetrari.vlad.rts.R
import chetrari.vlad.rts.app.extensions.load
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.data.network.model.Species
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_species.*

class SpeciesAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val speciesMapper: (Species) -> LiveData<Event<Species>>
) : ListAdapter<Species, SpeciesAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_species, parent, false),
        lifecycleOwner,
        speciesMapper
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(
        override val containerView: View,
        private val lifecycleOwner: LifecycleOwner,
        private val speciesMapper: (Species) -> LiveData<Event<Species>>
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(species: Species) {
            name.text = species.name
            species.thumbnailImageLink?.let(::setThumbnail) ?: speciesMapper(species).observe(lifecycleOwner) {
                when (it) {
                    Event.Progress -> progress.isVisible = true
                    is Event.Success -> it.result.thumbnailImageLink?.let(::setThumbnail)
                    is Event.Error -> progress.isVisible = false
                }
            }
        }

        private fun setThumbnail(url: String) {
            thumbnail.load(url)
            progress.isVisible = false
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Species>() {
        override fun areItemsTheSame(oldItem: Species, newItem: Species) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Species, newItem: Species) = oldItem == newItem
    }
}