package chetrari.vlad.redlist.app.main.images

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.WHITE
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.app.Intents
import chetrari.vlad.redlist.app.extensions.setNavIconColor
import chetrari.vlad.redlist.app.extensions.toggleVisibilityAnimated
import kotlinx.android.synthetic.main.activity_image_gallery.*
import kotlinx.android.synthetic.main.toolbar_black_transparent.*

class ImageGalleryActivity : AppCompatActivity(R.layout.activity_image_gallery) {

    private val args by navArgs<ImageGalleryActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK
        setSupportActionBar(toolbar.apply {
            title = args.title
            setNavigationOnClickListener { onBackPressed() }
        })
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        toolbar.setNavIconColor(WHITE)
        pager.adapter = ImageGalleryAdapter { toolbar.toggleVisibilityAnimated() }
            .also { it.submitList(args.images.toList()) }
        pagerIndicator.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu?) = trueAnd { menuInflater.inflate(R.menu.menu_gallery, menu) }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> trueAnd(::onBackPressed)
        R.id.share -> trueAnd(::share)
        else -> super.onOptionsItemSelected(item)
    }

    private fun share() {
        val imageUrl = args.images[pager.currentItem].url
        startActivity(Intent.createChooser(Intents.shareText(imageUrl), getString(R.string.share)))
    }

    private inline fun trueAnd(action: () -> Unit): Boolean {
        action()
        return true
    }

    override fun onDestroy() {
        pager.adapter = null
        super.onDestroy()
    }
}