package de.tp.hillforts.main

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import de.tp.hillforts.helpers.AuthProvider
import de.tp.hillforts.models.hillfort.HillfortFireStore
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
    var cachePreviousImage: Int? = null // cache previous image ID to be used when Image is changed and fragment is destroyed
    var cacheEditMode: Boolean? = null

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        //AuthProvider.init(applicationContext) not required for firebase
        hillforts = HillfortFireStore(applicationContext)
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