package de.tp.hillforts.views.hillfortDetails

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import de.tp.hillforts.R
import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.hillford_list_view_portrait.toolbar
import kotlinx.android.synthetic.main.hillfort_details_view_portrait.*
import org.jetbrains.anko.error
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HillfortDetailsView : BaseView() {

    lateinit var presenter: HillfortDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hillfort_details_view_portrait)

        // init presenter
        presenter = initPresenter(HillfortDetailsPresenter(this)) as HillfortDetailsPresenter

        // init toolbar
        init(toolbar, true)
    }

    override fun showHillfort(hillfort: HillfortModel) {
        hillfort.also {
            etName.setText(it.name)
            etDescription.setText(it.desc)
            mltNotes.setText(it.notes)
            cbVisited.isChecked = it.visited
            showDateVisited(it.dateVisited)
        }
    }

    fun onVisitedClicked(view: View){
        if (view is CheckBox){
            presenter.doHillfordVisited(cbVisited.isChecked)
        }
    }

    override fun showDateVisited(date: Date?){
        if(date != null){
            tvVisitedOn.text = SimpleDateFormat(dateFormat, Locale.GERMANY).format(date)
            cbVisited.setText(R.string.cb_visited_on)
        }
        else{
            cbVisited.setText(R.string.cb_not_yet_visited)
            tvVisitedOn.text = ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemSave -> {
                val name = etName.text.toString()
                val desc = etDescription.text.toString()
                val notes = mltNotes.text.toString()
                val visited = cbVisited.isChecked
                val visitedOn = tvVisitedOn.text.toString()
                var visitedDate = Date()
                if(visited && visitedOn != ""){
                    try {
                        visitedDate = SimpleDateFormat(dateFormat, Locale.GERMANY).parse(visitedOn)
                    } catch (e: ParseException){
                        // should never happen as date will always be populated automatically!
                        error("Visited date was populated incorrectly! Error:\n$e")
                        presenter.doAddOrSave(name, desc, notes, visited, null)
                    }
                }
                presenter.doAddOrSave(name, desc, notes, visited, visitedDate)
            }
            R.id.itemCancel -> presenter.doCancel()
            R.id.itemDelete -> presenter.doDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort_details, menu)
        return super.onCreateOptionsMenu(menu)
    }
}