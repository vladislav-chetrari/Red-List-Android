package chetrari.vlad.rts.app.main.countries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import chetrari.vlad.rts.R
import chetrari.vlad.rts.data.network.model.Country
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_country.*

class CountriesAdapter(
    private val onItemSelected: (Country) -> Unit
) : ListAdapter<Country, CountriesAdapter.ViewHolder>(ItemCallback()) {

    var countryInProgress: Country? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_country, parent, false),
        onItemSelected
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = getItem(position).run {
        holder.bind(this, this == countryInProgress)
    }

    class ViewHolder(
        override val containerView: View,
        private val onClick: (Country) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(country: Country, inProgress: Boolean) {
            name.text = country.name
            progress.isVisible = inProgress
            containerView.setOnClickListener { onClick(country) }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country) = oldItem.isoCode == newItem.isoCode
        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean = oldItem == newItem
    }
}