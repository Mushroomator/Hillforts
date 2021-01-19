package de.tp.hillforts.views.search

import android.app.SearchManager
import android.content.Intent
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BasePresenter
import org.jetbrains.anko.info
import java.text.SimpleDateFormat
import java.util.*

class SearchHillfortsPresenter(view: SearchHillfortsView): BasePresenter(view) {

    init{
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == view.intent.action) {
            view.intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doSearch(query)
            }
        }
    }

    fun doSearch(query: String): List<HillfortModel> {
        val all = app.hillforts.findAll()
        all.parallelStream().filter{ containsQuery(query, it) }
        return all
    }

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
                    return SimpleDateFormat(view?.dateFormat, Locale.GERMANY).format(hillfortModel.dateVisited).equals("${dateComponents[0]}/${dateComponents[1]}/{$dateComponents[2]}")
                }
            }
        }
        return false
    }
}