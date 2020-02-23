package chetrari.vlad.rts.app.main.countries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import chetrari.vlad.rts.R
import chetrari.vlad.rts.data.network.model.Country
import kotlinx.android.extensions.LayoutContainer

class CountriesAdapter : ListAdapter<Country, CountriesAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_country, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(country: Country) {
            containerView.findViewById<TextView>(R.id.name).text = country.name
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country) = oldItem.isoCode == newItem.isoCode
        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean = oldItem == newItem
    }
}