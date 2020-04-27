package chetrari.vlad.rts.app.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import chetrari.vlad.rts.app.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO revert
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}