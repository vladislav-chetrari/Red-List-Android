package chetrari.vlad.redlist.app.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val navController: NavController by lazy { findNavController(this, R.id.mainHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavBar.setupWithNavController(navController)
    }
}