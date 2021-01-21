package de.tp.hillforts.main

import android.app.Application
import de.tp.hillforts.helpers.AuthProvider
import de.tp.hillforts.models.hillfort.HillfortJsonRepo
import de.tp.hillforts.models.hillfort.HillfortMemRepo
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.models.hillfort.IHillfortRepo
import de.tp.hillforts.models.user.UserModel

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

    lateinit var hillforts: IHillfortRepo
    var hillfortCache: HillfortModel? = null    // required to cache hillfort when HillfortDetails activity is stopped when Back (Up Support) is clicked on edit location

    override fun onCreate() {
        super.onCreate()
        AuthProvider.init(applicationContext)
        info("Placemark app started")
    }

    /**
     * Initialize the JSON Repository. Load user-specific file.
     * @param user logged in user
     */
    fun initHillfortRepo(user: UserModel){
        hillforts = HillfortJsonRepo(applicationContext, user.id)
    }
}