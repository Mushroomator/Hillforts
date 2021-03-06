package de.tp.hillforts.views.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.hillfordList.HillfortListFragmentDirections
import de.tp.hillforts.views.hillfortDetails.HillfortDetailsFragmentDirections
import de.tp.hillforts.views.login.LoginFragmentDirections
import kotlinx.android.synthetic.main.main_fragment.*

class MainActivity : BaseView(), NavController.OnDestinationChangedListener{

    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_fragment)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.hillfortListFragment, R.id.hillfortMapFragment), drawerLayout)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // handle clicks on navigation bar menu items automatically
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(this)
    }


    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(this)
    }

    /**
     * Callback for when the [.getCurrentDestination] or its arguments change.
     * This navigation may be to a destination that has not been seen before, or one that
     * was previously on the back stack. This method is called after navigation is complete,
     * but associated transitions may still be playing.
     *
     * @param controller the controller that navigated
     * @param destination the new destination
     * @param arguments the arguments passed to the destination
     */
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if(destination.id == R.id.loginFragment){
            supportActionBar?.hide()
        }else{
            supportActionBar?.show()
        }
    }

}