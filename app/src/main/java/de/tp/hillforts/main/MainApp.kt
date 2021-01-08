package de.tp.hillforts.main

import android.app.Application
import de.tp.hillforts.models.HillfortMemRepo
import de.tp.hillforts.models.IHillfortRepo

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

    lateinit var hillforts: IHillfortRepo

    override fun onCreate() {
        super.onCreate()
        info("Placemark app started")
        hillforts = HillfortMemRepo()
    }
}