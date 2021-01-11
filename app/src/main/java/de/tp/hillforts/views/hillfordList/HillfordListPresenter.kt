package de.tp.hillforts.views.hillfordList

import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.info

class HillfordListPresenter(view: BaseView): BasePresenter(view) {


    /**
     * Load hillforts from Repo and display them on screen.
     * @author Thomas Pilz
     */
    fun loadPlacemarks(): Unit{
        val hillforts = app.hillforts.findAll()
        view?.showHillforts(hillforts)
    }

    /**
     * Logout a logged in user.
     * @author Thomas Pilz
     */
    fun doLogout(){
        view?.info("User logged out.")
        view?.navigateTo(VIEW.LOGIN)
    }

    /**
     * Call another activity to add a hillfort.
     * @author Thomas Pilz
     */
    fun doAddHillfort(){

    }
}