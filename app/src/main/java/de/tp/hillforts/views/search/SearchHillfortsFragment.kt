package de.tp.hillforts.views.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.hillfordList.HillfortListener
import de.tp.hillforts.views.reusable.HillfortAdapter
import kotlinx.android.synthetic.main.login_view.*
import kotlinx.android.synthetic.main.login_view.view.*
import kotlinx.android.synthetic.main.search_hillforts_view.*
import org.jetbrains.anko.toast

class SearchHillfortsFragment: Fragment(), HillfortListener {

    lateinit var presenter: SearchHillfortFragmentPresenter
    lateinit var hostView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hostView = inflater.inflate(R.layout.search_hillforts_view, container, false)

        setHasOptionsMenu(true)

        return hostView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init presenter
        presenter = SearchHillfortFragmentPresenter(this)

        rvSearchResults.layoutManager = LinearLayoutManager(context)
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_hillforts, menu)

        if(menu != null){
            // Get the SearchView and set the searchable configuration
            val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            (menu.findItem(R.id.itemAppBarSearch).actionView as SearchView).apply {
                // Assumes current activity is the searchable activity
                setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                isSubmitButtonEnabled = true
                isQueryRefinementEnabled = true
                isIconifiedByDefault = false
                setQuery(presenter.query, false)    // do not submit query as it has already been submitted, just show what user has entered
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}