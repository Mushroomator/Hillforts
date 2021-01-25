package de.tp.hillforts.views.hillfordList

import androidx.navigation.fragment.findNavController
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BasePresenterFragment

class HillfortListFragmentPresenter(var view: HillfortListFragment): BasePresenterFragment(view) {

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
        view.showHillforts(hillforts)
    }

    fun loadFavourites(){
        val hillforts = app.hillforts.findAll().filter { it.isFavourite }
        tabSelected = 1
        view.showHillforts(hillforts)
    }

    /**
     * Call another activity to add a hillfort.
     * @author Thomas Pilz
     */
    fun doAddHillfort(){
        view.findNavController().navigate(R.id.hillfortDetailsFragment)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        val action = HillfortListFragmentDirections.listToDetails(photo = null, hillfort = hillfort)
        view.findNavController().navigate(action)
    }
}