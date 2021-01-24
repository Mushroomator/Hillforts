package de.tp.hillforts.views.hillfordList

import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortFireStore
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BasePresenterFragment
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.toast

class HillfortListFragmentPresenter(var view: HillfortListFragment?): BasePresenterFragment(view) {

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
        hostActivity?.navigateTo(VIEW.DETAILS)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        hostActivity?.navigateTo(VIEW.DETAILS, 0, HILLFORT_EDIT, hillfort)
    }
}