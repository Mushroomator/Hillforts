package de.tp.hillforts.views.search

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.navigation.ActivityNavigatorDestinationBuilder
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.VIEW
import de.tp.hillforts.views.mainActivity.MainActivity
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

class SearchHillfortsPresenter(view: SearchHillfortsView): BasePresenter(view) {
/*
    private val HILLFORT_EDIT = "hillfort_edit"
    var query = ""

    init{

        val start = System.currentTimeMillis()
        this.view = view
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == view.intent.action) {
            // start search and measure the elapsed time
            view.intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                this.query = query
                doSaveRecentQuery(query)
                val results = doSearch(query)
                val end = System.currentTimeMillis()
                val elapsed = (end - start) / 1000f  // elapsed time in seconds
                view.showResults(results, elapsed)
            }
        }
    }

    /**
     * Save recent query for Android to suggest recent queries automatically.
     * @param query Search query entered by user
     * @author Thomas Pilz
     */
    fun doSaveRecentQuery(query: String){
        SearchRecentSuggestions(view, HillfortSuggestionProvider.AUTHORITY, HillfortSuggestionProvider.MODE)
            .saveRecentQuery(query, null)
    }

    /**
     * Call activity HillfortDetails
     * @param hillfort hillfort to be shown
     * @author Thomas Pilz
     */
    fun doEditHillfort(hillfort: HillfortModel){
        val intent = Intent(view!!, MainActivity::class.java)
        intent.putExtra("hillfort", hillfort)
        view?.startActivity(intent)
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
                    val hillfortDate = SimpleDateFormat(view?.dateFormat, Locale.GERMANY).format(hillfortModel.dateVisited)
                    val dateStr = "${dateComponents[0]}/${dateComponents[1]}/${dateComponents[2]}"
                    return hillfortDate.equals(dateStr)
                }
            }
        }
        return false
    }

 */
}