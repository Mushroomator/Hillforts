package de.tp.hillforts.views.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.reusable.HillfortAdapter
import de.tp.hillforts.views.hillfordList.HillfortListener
import kotlinx.android.synthetic.main.search_hillforts_view.*

class SearchHillfortsView : BaseView(), HillfortListener {

    lateinit var presenter: SearchHillfortsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_hillforts_view)

        presenter = initPresenter(SearchHillfortsPresenter(this)) as SearchHillfortsPresenter

        init(toolbar, true)

        rvSearchResults.layoutManager = LinearLayoutManager(this)
    }

    fun showResults(hillforts: List<HillfortModel>, time: Float){
        tvResults.text = resources.getQuantityString(R.plurals.results, hillforts.size, hillforts.size, time)
        rvSearchResults.adapter = HillfortAdapter(hillforts, this)
        rvSearchResults.adapter?.notifyDataSetChanged()
    }

    /**
     * Executed when a hillford is clicked on in recycler view.
     * @param hillford hillford which was clicked on
     */
    override fun onHillfordClick(hillford: HillfortModel) {
        presenter.doEditHillfort(hillford)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_hillforts, menu)

        if(menu != null){
            // Get the SearchView and set the searchable configuration
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            (menu.findItem(R.id.itemAppBarSearch).actionView as SearchView).apply {
                // Assumes current activity is the searchable activity
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                isSubmitButtonEnabled = true
                isQueryRefinementEnabled = true
                isIconifiedByDefault = false
                setQuery(presenter.query, false)    // do not submit query as it has already been submitted, just show what user has entered
            }
        }
        return super.onCreateOptionsMenu(menu)
    }
}