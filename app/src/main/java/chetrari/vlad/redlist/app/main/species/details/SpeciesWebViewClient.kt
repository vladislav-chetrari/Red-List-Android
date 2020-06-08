package chetrari.vlad.redlist.app.main.species.details

import android.webkit.WebView
import android.webkit.WebViewClient

class SpeciesWebViewClient : WebViewClient() {
    override fun onPageFinished(view: WebView, url: String?) = view.captHtml()
    override fun onPageCommitVisible(view: WebView, url: String?) = view.captHtml()

    override fun onLoadResource(view: WebView, url: String?) {
        super.onLoadResource(view, url)
        view.captHtml()
    }

    private fun WebView.captHtml() = loadUrl(
        "javascript:window.SpeciesJSInterface.returnHtml" +
                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"
    )
}