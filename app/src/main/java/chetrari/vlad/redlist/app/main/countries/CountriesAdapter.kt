package chetrari.vlad.redlist.app.main.countries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.extensions.load
import chetrari.vlad.redlist.data.persistence.model.Country
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_country.*

class CountriesAdapter(
    private val onItemSelected: (Country) -> Unit
) : ListAdapter<Country, CountriesAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_country, parent, false),
        onItemSelected
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(
        override val containerView: View,
        private val onClick: (Country) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(country: Country) {
            name.text = country.name
            flagImage.load(containerView.resources.getString(R.string.country_flag_icon, country.isoCode))
            containerView.setOnClickListener { onClick(country) }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country) = oldItem.isoCode == newItem.isoCode
        override fun areContentsTheSame(oldItem: Country, newItem: Country) = oldItem == newItem
    }
}