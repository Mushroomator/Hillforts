package de.tp.hillforts.views.hillfordList

import de.tp.hillforts.R
import de.tp.hillforts.helpers.AuthProvider
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.info

class HillfordListPresenter(view: BaseView): BasePresenter(view) {

    private val HILLFORT_EDIT = "hillfort_edit"

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
        AuthProvider.logout()
        view?.info(view?.getString(R.string.log_signed_out))
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doShowSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }

    /**
     * Call another activity to add a hillfort.
     * @author Thomas Pilz
     */
    fun doAddHillfort(){
        view?.navigateTo(VIEW.DETAILS)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        view?.navigateTo(VIEW.DETAILS, 0, HILLFORT_EDIT, hillfort)
    }
}