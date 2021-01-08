package de.tp.hillforts.main

import android.app.Application
import de.tp.hillforts.models.HillfortMemRepo
import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.models.IHillfortRepo

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

    lateinit var hillforts: IHillfortRepo

    override fun onCreate() {
        super.onCreate()
        info("Placemark app started")
        hillforts = HillfortMemRepo()
        hillforts.create(HillfortModel(name = "1", desc = "desc", visited = false))
        hillforts.create(HillfortModel(name = "2", desc = "desc", visited = true))
        info("All hillforts:")
        hillforts.findAll().forEach {
            info(it.toString())
        }
    }
}