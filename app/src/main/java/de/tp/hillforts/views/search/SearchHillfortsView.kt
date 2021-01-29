package de.tp.hillforts.views.search

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.search_hillforts_fragment_view.*


class SearchHillfortsView : BaseView() //, HillfortListener
{

    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    //lateinit var presenter: SearchHillfortsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.search_hillforts_fragment_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostSearch) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(toolbarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //setupActionBarWithNavController(navController, appBarConfiguration)

        // handle clicks on navigation bar menu items automatically
        //navViewSearch.setupWithNavController(navController)

        /*
        setContentView(R.layout.search_hillforts_view)

        presenter = initPresenter(SearchHillfortsPresenter(this)) as SearchHillfortsPresenter

        init(toolbar, true)^

       rvSearchResults.layoutManager = LinearLayoutManager(this)

         */
    }

    /*
    fun showResults(hillforts: List<HillfortModel>, time: Float){
        tvResults.text = resources.getQuantityString(R.plurals.results, hillforts.size, hillforts.size, time)
        rvSearchResults.adapter = HillfortAdapter(hillforts, this)
        rvSearchResults.adapter?.notifyDataSetChanged()
    }

    /**
     * Executed when a hillford is clicked on in recycler view.
     * @param hillfort hillford which was clicked on
     */
    override fun onHillfordClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
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

     */
}