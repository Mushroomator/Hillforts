package de.tp.hillforts.views.hillfordList

import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView

class HillfordListPresenter(view: BaseView): BasePresenter(view) {


    /**
     * Load hillforts from Repo and display them on screen.
     * @author Thomas Pilz
     */
    fun loadPlacemarks(): Unit{
        val hillforts = app.hillforts.findAll()
        view?.showHillforts(hillforts)
    }
}