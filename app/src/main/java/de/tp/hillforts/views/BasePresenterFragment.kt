package de.tp.hillforts.views

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.main.MainApp
import org.jetbrains.anko.info

abstract class BasePresenterFragment(var baseView: Fragment?) {

    var hostActivity = baseView?.activity as BaseView?
    var app: MainApp = hostActivity?.application as MainApp

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    }

    open fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
    }

    /**
     * Set view to null when acitivity is destroyed.
     */
    open fun onDestroy() {
        hostActivity = null
    }

    /**
     * Logout a logged in user.
     * @author Thomas Pilz
     */
    fun doLogout() {
        //AuthProvider.logout() not required for Firebase
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        hostActivity?.info(hostActivity?.getString(R.string.log_signed_out))
        baseView?.findNavController()?.navigate(R.id.listToDetails)
    }

    /**
     * Navigate to List of Hillforts
     * @author Thomas Pilz
     */
    fun doShowAllHillforts() {
        baseView?.findNavController()?.navigate(R.id.hillfortListFragment)
    }

    /**
     * Navigate to Map of all hillforts
     * @author Thomas Pilz
     */
    fun doShowMap() {
        baseView?.findNavController()?.navigate(R.id.globalMap)
    }

    /**
     * Navigate to settings.
     * @author Thomas Pilz
     */
    fun doShowSettings() {
        baseView?.findNavController()?.navigate(R.id.settingsFragment)
    }
}