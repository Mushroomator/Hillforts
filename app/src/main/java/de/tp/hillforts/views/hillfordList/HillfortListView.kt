package de.tp.hillforts.views.hillfordList

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.VIEW
import de.tp.hillforts.views.reusable.HillfortAdapter
import de.tp.hillforts.views.search.SearchHillfortsView
import kotlinx.android.synthetic.main.hillfort_list_view.*

class HillfortListView : BaseView(), HillfortListener, TabLayout.OnTabSelectedListener,
    SearchView.OnQueryTextListener {

    lateinit var presenter: HillfortListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hillfort_list_view)

        // init presenter
        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

        // init toolbar
        init(toolbar, true) //disable up-support later; just for test purposes

        // init navigation drawer
        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }
        val defaultItem = navView.menu.getItem(0)
        if (defaultItem != null) {
            defaultItem.isChecked = true
        }
        navView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            when(menuItem.itemId){
                R.id.itemNavAllHillforts -> { drawerLayout.close() }
                R.id.itemNavMap -> { presenter.doShowMap() }
                R.id.itemNavSettings -> { presenter.doShowSettings() }
            }
            menuItem.isChecked = true
            drawerLayout.close()
            true
        }

        // init tab listeners
        tabLayout.addOnTabSelectedListener(this)

        // init recycler view
        rvHillforts.layoutManager = LinearLayoutManager(this)

        presenter.loadHillforts()
    }

    /**
     * Executed by HillfordViewHolder::bind() when a hillford is clicked on.
     * @param hillford hillford which was clicked on
     */
    override fun onHillfordClick(hillford: HillfortModel) {
        presenter.doEditHillfort(hillford)
    }

    /**
     * Show all hillforts on the screen.
     * @param hillforts hillforts to be displayed
     * @author Thomas Pilz
     */
    override fun showHillforts(hillforts: List<HillfortModel>) {
        rvHillforts.adapter = HillfortAdapter(hillforts, this)
        rvHillforts.adapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemSave -> presenter.doAddHillfort()
            R.id.itemLogout -> presenter.doLogout()
            R.id.itemSettings -> presenter.doShowSettings()
            R.id.itemMap -> presenter.doShowMap()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_view, menu)

        if (menu != null) {
            // Get the SearchView and set the searchable configuration
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            (menu.findItem(R.id.itemAppBarSearch).actionView as SearchView).apply {
                // Assumes current activity is the searchable activity
                setSearchableInfo(
                    searchManager.getSearchableInfo(
                        ComponentName(
                            this.context,
                            SearchHillfortsView::class.java
                        )
                    )
                )
                isSubmitButtonEnabled = true
                isQueryRefinementEnabled = true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //presenter.loadHillforts() // notify recyclerView that data has been changed --> not required anymore as on resume will load data anyway
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.doLogout()
    }

    override fun onResume() {
        super.onResume()
        presenter.doLoadData()
    }

    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     */
    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
            if (tab.position == 1) {
                presenter.loadFavourites()
            } else {
                presenter.loadHillforts()
            }
        }
    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     */
    override fun onTabUnselected(tab: TabLayout.Tab?) {
        /* no-op */
    }

    /**
     * Called when a tab that is already selected is chosen again by the user. Some applications may
     * use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     */
    override fun onTabReselected(tab: TabLayout.Tab?) {
        /* no-op */
    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     *
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     *
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}
