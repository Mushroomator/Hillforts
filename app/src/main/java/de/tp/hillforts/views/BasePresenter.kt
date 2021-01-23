package de.tp.hillforts.views

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.main.MainApp
import org.jetbrains.anko.info

abstract class BasePresenter(var view: BaseView?) {

    var app: MainApp = view?.application as MainApp

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    }

    open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    }

    /**
     * Set view to null when acitivity is destroyed.
     */
    open fun onDestroy(){
        view = null
    }

    /**
     * Logout a logged in user.
     * @author Thomas Pilz
     */
    fun doLogout(){
        //AuthProvider.logout() not required for Firebase
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.info(view?.getString(R.string.log_signed_out))
        view?.navigateTo(VIEW.LOGIN)
    }

    /**
     * Navigate to List of Hillforts
     * @author Thomas Pilz
     */
    fun doShowAllHillforts(){
        view?.navigateTo(VIEW.LIST)
    }

    /**
     * Navigate to Map of all hillforts
     * @author Thomas Pilz
     */
    fun doShowMap(){
        view?.navigateTo(VIEW.MAP)
    }

    /**
     * Navigate to settings.
     * @author Thomas Pilz
     */
    fun doShowSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }

}