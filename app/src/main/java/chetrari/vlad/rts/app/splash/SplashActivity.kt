package chetrari.vlad.rts.app.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import chetrari.vlad.rts.app.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) = proceed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.addObserver(lifecycleObserver)
        super.onCreate(savedInstanceState)
    }

    private fun proceed() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        lifecycle.removeObserver(lifecycleObserver)
        finish()
    }
}