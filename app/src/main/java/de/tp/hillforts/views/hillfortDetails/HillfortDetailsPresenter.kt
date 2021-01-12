package de.tp.hillforts.views.hillfortDetails

import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import java.util.*

class HillfortDetailsPresenter(view: BaseView): BasePresenter(view) {

    private val HILLFORT_EDIT = "hillford_edit"

    var hillfort = HillfortModel()

    init {

        if(view.intent.hasExtra(HILLFORT_EDIT) && view.intent.extras?.getParcelable<HillfortModel>(HILLFORT_EDIT) != null){
            hillfort = view.intent.extras?.getParcelable<HillfortModel>(HILLFORT_EDIT)!!    //smart cast not possible
            view.showHillfort(hillfort)
        }
    }

    fun doHillfordVisited(visited: Boolean){
        if(visited){
            hillfort.visited = true
            hillfort.dateVisited = Date()
        }
        else{
            hillfort.visited = false
            hillfort.dateVisited = null
        }
        view?.showDateVisited(hillfort.dateVisited)
    }

    fun doAddOrSave(name: String, desc: String, notes: String, visited: Boolean, visitedOn: Date?){
        hillfort.also{
            it.name = name
            it.desc = desc
            it.notes = notes
            it.visited = visited
            it.dateVisited = visitedOn
        }
        app.hillforts.create(hillfort)
        view?.finish()
    }

    fun doCancel(){
        view?.finish()
    }

    fun doDelete(){
        app.hillforts.delete(hillfort)
        view?.finish()
    }
}