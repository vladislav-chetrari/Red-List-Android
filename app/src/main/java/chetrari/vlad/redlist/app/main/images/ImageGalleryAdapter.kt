package chetrari.vlad.redlist.app.main.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.extensions.load
import chetrari.vlad.redlist.data.persistence.model.SpeciesImage
import kotlinx.android.synthetic.main.item_image.view.*

class ImageGalleryAdapter(
    private val onImageClick: () -> Unit
) : PagerAdapter() {

    private var data = emptyList<SpeciesImage>()

    override fun instantiateItem(parent: ViewGroup, position: Int): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        val image = data[position]
        view.image.load(image.url)
        view.setOnClickListener { onImageClick() }
        parent.addView(view)
        return view
    }

    override fun getCount() = data.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) = container.removeView(`object` as View)

    fun submitList(list: List<SpeciesImage>) {
        data = list
        notifyDataSetChanged()
    }
}