package de.tp.hillforts.views.hillfordList

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.reusable.HillfortAdapter
import de.tp.hillforts.views.search.SearchHillfortsView
import kotlinx.android.synthetic.main.hillfort_list_view.*
import kotlinx.android.synthetic.main.login_view.*
import kotlinx.android.synthetic.main.login_view.view.*
import org.jetbrains.anko.toast

class HillfortListFragment: Fragment(), HillfortListener, TabLayout.OnTabSelectedListener {

    lateinit var presenter: HillfortListFragmentPresenter
    lateinit var hostView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hostView = inflater.inflate(R.layout.hillfort_list_view, container, false)

        setHasOptionsMenu(true)

        // init presenter
        presenter = HillfortListFragmentPresenter(this)

        return hostView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // init tab listeners
        tabLayout.addOnTabSelectedListener(this)

        // init recycler view
        rvHillforts.layoutManager = LinearLayoutManager(this.context)

        presenter.loadHillforts()

        super.onViewCreated(view, savedInstanceState)
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
    fun showHillforts(hillforts: List<HillfortModel>) {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_view, menu)

        if (menu != null) {
            // Get the SearchView and set the searchable configuration
            val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*
    override fun onBackPressed() {
        super.onBackPressed()
        presenter.doLogout()
    }

     */

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

}