package chetrari.vlad.rts.app.main

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import chetrari.vlad.rts.R
import chetrari.vlad.rts.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val navController: NavController by lazy { findNavController(this, R.id.navigationHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavBar.setupWithNavController(navController)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        toolbar ?: return
        val setupToolbarWithNavController = { toolbar.setupWithNavController(navController) }
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) setupToolbarWithNavController()
        else lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                setupToolbarWithNavController()
                owner.lifecycle.removeObserver(this)
            }
        })
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}