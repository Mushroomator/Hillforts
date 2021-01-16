package de.tp.hillforts.views.settings

import de.tp.hillforts.views.BasePresenter

class SettingsPresenter(view: SettingsView): BasePresenter(view) {


    fun doLoadUser(){
        val email = "Testmail"
        val password = "TestPass"
        if(view is SettingsView){
            (view as SettingsView).showUserInfo(email, password)
        }
    }

    fun doLoadStatistics(){
        val hillforts = app.hillforts.findAll()
        var visited = 0
        hillforts.forEach{ if (it.dateVisited != null) visited++ }

        if(view is SettingsView){
            (view as SettingsView).showStatistics(hillforts.size, visited)
        }
    }
}