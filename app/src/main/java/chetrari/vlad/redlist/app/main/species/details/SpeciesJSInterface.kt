package chetrari.vlad.redlist.app.main.species.details

import android.webkit.JavascriptInterface

class SpeciesJSInterface(
    private val onReceivePageContent: (String) -> Unit
) {

    @JavascriptInterface
    fun returnHtml(html: String) {
        onReceivePageContent(html)
    }

    companion object {
        const val NAME = "SpeciesJSInterface"
    }
}