package de.tp.hillforts.views.hillfortDetails

import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import org.jetbrains.anko.info
import java.util.*

class HillfortDetailsPresenter(view: BaseView): BasePresenter(view) {

    private val HILLFORT_EDIT = "hillford_edit"

    var hillfort = HillfortModel()
    var editMode = false

    init {

        if(view.intent.hasExtra(HILLFORT_EDIT) && view.intent.extras?.getParcelable<HillfortModel>(HILLFORT_EDIT) != null){
            editMode = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>(HILLFORT_EDIT)!!    //smart cast not possible
            view.showHillfort(hillfort)
        }
    }

    fun doHillfordVisited(visited: Boolean){
        if(visited){
            hillfort.dateVisited = Date()
        }
        else{
            hillfort.dateVisited = null
        }
        view?.showDateVisited(hillfort.dateVisited)
    }

    fun doAddOrSave(name: String, desc: String, notes: String, visitedOn: Date?){
        view?.info("Value of visitedOn: $visitedOn")
        hillfort.also{
            it.name = name
            it.desc = desc
            it.notes = notes
            it.dateVisited = visitedOn
        }
        if(editMode){
            app.hillforts.update(hillfort)
        }
        else{
            app.hillforts.create(hillfort)
        }
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