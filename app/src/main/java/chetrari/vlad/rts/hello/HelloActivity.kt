package chetrari.vlad.rts.hello

import android.os.Bundle
import androidx.core.view.isVisible
import chetrari.vlad.rts.R
import chetrari.vlad.rts.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_hello.*

class HelloActivity : BaseActivity(R.layout.activity_hello) {
    private val viewModel by provide<HelloViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.greeting.observe(onProgress = { onAppNameProgress() }, onError = ::onAppNameFails) {
            textView.text = it
            onAppNameProgress(false)
        }
    }

    private fun onAppNameProgress(inProgress: Boolean = true) {
        textView.isVisible = inProgress.not()
        progressBar.isVisible = inProgress
    }

    private fun onAppNameFails(error: Throwable) {
        Snackbar.make(container, error.message ?: "undefined", Snackbar.LENGTH_SHORT).show()
        onAppNameProgress(false)
    }
}