package de.tp.hillforts.main

import android.app.Application
import de.tp.hillforts.models.hillfort.HillfortMemRepo
import de.tp.hillforts.models.hillfort.IHillfortRepo

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