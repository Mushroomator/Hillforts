package de.tp.hillforts.views.search

import android.app.SearchManager
import android.content.Intent
import android.provider.SearchRecentSuggestions
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortFireStore
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BasePresenterFragment
import de.tp.hillforts.views.VIEW
import de.tp.hillforts.views.hillfordList.HillfortListFragmentDirections
import de.tp.hillforts.views.mainActivity.MainActivity
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

class SearchHillfortFragmentPresenter(var view: SearchHillfortsFragment?): BasePresenterFragment(view) {

    private val HILLFORT_EDIT = "hillfort_edit"
    var query = ""
    val dateFormat = "dd/MM/yyyy"

    init{

        val start = System.currentTimeMillis()
        this.view = view
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == view!!.activity?.intent?.action) {
            // start search and measure the elapsed time
            view!!.activity?.intent?.getStringExtra(SearchManager.QUERY)?.also { query ->
                this.query = query
                doSaveRecentQuery(query)
                val results = doSearch(query)
                val end = System.currentTimeMillis()
                val elapsed = (end - start) / 1000f  // elapsed time in seconds
                view!!.showResults(results, elapsed)
            }
        }
    }

    /**
     * Save recent query for Android to suggest recent queries automatically.
     * @param query Search query entered by user
     * @author Thomas Pilz
     */
    fun doSaveRecentQuery(query: String){
        SearchRecentSuggestions(view!!.activity, HillfortSuggestionProvider.AUTHORITY, HillfortSuggestionProvider.MODE)
            .saveRecentQuery(query, null)
    }

    /**
     * Call activity HillfortDetails
     * @param hillfort hillfort to be shown
     * @author Thomas Pilz
     */
    fun doEditHillfort(hillfort: HillfortModel){
        // use main navGraph and go to edit location to provide access to other views again!
        val navController = view!!.findNavController()
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = R.id.hillfortListFragment
        navController.graph = navGraph
        val action = HillfortListFragmentDirections.listToDetails(photo = null, location = null, hillfort = hillfort)
        view?.findNavController()?.navigate(action)
    }

    /**
     * Searches for a query string in all hillforts.
     * @param query query string entered by user
     * @author Thomas Pilz
     */
    fun doSearch(query: String): List<HillfortModel> {
        val all = app.hillforts.findAll()
        return all.parallelStream()
            .filter{ containsQuery(query, it) }
            .collect(Collectors.toList())
    }


    /**
     * Checkf is a hillfort contains the query string.
     * @param query query string entered by the user
     * @author Thomas Pilz
     */
    fun containsQuery(query: String, hillfortModel: HillfortModel): Boolean{
        if (hillfortModel.name.contains(query, ignoreCase = true) ||
            hillfortModel.desc.contains(query, ignoreCase = true) ||
            hillfortModel.notes.contains(query, ignoreCase = true)){
            return true
        }
        // check if query contains a date
        if(hillfortModel.dateVisited != null){
            val queryDate = Regex("\\d{2}[-.\\/]\\d{2}(?:[-.\\/]\\d{2}(\\d{2})?)?") // check if there is a date in the queryString
                .find(query)
            if (queryDate != null){
                // check if date actually matches visitedOn date
                val dateComponents = query.split(Regex("[-.\\/]"))
                if(dateComponents.size == 3){
                    val hillfortDate = SimpleDateFormat(dateFormat, Locale.GERMANY).format(hillfortModel.dateVisited)
                    val dateStr = "${dateComponents[0]}/${dateComponents[1]}/${dateComponents[2]}"
                    return hillfortDate.equals(dateStr)
                }
            }
        }
        return false
    }
}