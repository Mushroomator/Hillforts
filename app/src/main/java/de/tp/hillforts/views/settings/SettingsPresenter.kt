package de.tp.hillforts.views.settings

import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.helpers.AuthProvider
import de.tp.hillforts.views.BasePresenter

class SettingsPresenter(view: SettingsView): BasePresenter(view) {

/*
    /**
     * Load user data.
     * At the moment just sample data.
     */
    fun doLoadUser(){
        /*
        val user = AuthProvider.currentUser
        if(view is SettingsView && user != null){
            (view as SettingsView).showUserInfo(user.email, user.password)
        }
        */
        // Password information is no longer available in Firebase. -> display email and uuid instead
        val user = FirebaseAuth.getInstance().currentUser
        if(view is SettingsView && user != null){
            (view as SettingsView).showUserInfo(user.email!!, user.uid)
        }
    }

    /**
     * Calculate a users statistics.
     */
    fun doLoadStatistics(){
        val hillforts = app.hillforts.findAll()
        var visited = 0
        hillforts.forEach{ if (it.dateVisited != null) visited++ }

        if(view is SettingsView){
            (view as SettingsView).showStatistics(hillforts.size, visited)
        }
    }

 */
}