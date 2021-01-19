package de.tp.hillforts.views.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView

class SearchHillfortsView : BaseView() {

    lateinit var presenter: SearchHillfortsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_search_hillfort)

        presenter = initPresenter(SearchHillfortsPresenter(this)) as SearchHillfortsPresenter
    }
}