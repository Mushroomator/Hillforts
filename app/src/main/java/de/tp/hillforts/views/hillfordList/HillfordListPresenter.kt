package de.tp.hillforts.views.hillfordList

import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView

class HillfordListPresenter(view: BaseView): BasePresenter(view) {


    fun loadPlacemarks(): List<HillfortModel>{
        return app.hillforts.findAll()
    }
}