package de.tp.hillforts.views.settings

import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortFireStore
import de.tp.hillforts.views.BasePresenterFragment
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.toast

class SettingsFragmentPresenter(var view: SettingsFragment?): BasePresenterFragment(view) {

    /**
     * Load user data.
     * @author Thomas Pilz
     */
    fun doLoadUser(){
        // Password information is no longer available in Firebase. -> display email and uuid instead
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            view?.showUserInfo(user.email!!, user.uid)
        }
    }

    /**
     * Calculate a users statistics.
     * @author Thomas Pilz
     */
    fun doLoadStatistics(){
        val hillforts = app.hillforts.findAll()
        var visited = 0
        hillforts.forEach{ if (it.dateVisited != null) visited++ }

        view?.showStatistics(hillforts.size, visited)
    }
}