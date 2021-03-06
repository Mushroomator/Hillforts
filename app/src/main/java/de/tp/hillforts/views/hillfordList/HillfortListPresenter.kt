package de.tp.hillforts.views.hillfordList

import android.content.Intent
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.helpers.AuthProvider
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.VIEW
import de.tp.hillforts.views.editLocation.EditLocationView
import org.jetbrains.anko.info

class HillfortListPresenter(view: BaseView): BasePresenter(view) {

    private val HILLFORT_EDIT = "hillfort_edit"
    private var tabSelected = 0

    /**
     * Loads either favourites or all hillforts depending on selected tab
     */
    fun doLoadData(){
        if(tabSelected == 0){
            loadHillforts()
        }
        else{
            loadFavourites()
        }
    }

    /**
     * Load hillforts from Repo and display them on screen.
     * @author Thomas Pilz
     */
    fun loadHillforts(): Unit{
        val hillforts = app.hillforts.findAll()
        tabSelected = 0
        view?.showHillforts(hillforts)
    }

    fun loadFavourites(){
        val hillforts = app.hillforts.findAll().filter { it.isFavourite }
        tabSelected = 1
        view?.showHillforts(hillforts)
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