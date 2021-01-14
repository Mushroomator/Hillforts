package de.tp.hillforts.views.hillfortDetails

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ActionMenuView
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.GoogleMap
import de.tp.hillforts.R
import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.hillford_list_view_portrait.toolbar
import kotlinx.android.synthetic.main.hillfort_details_view_portrait.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HillfortDetailsView : BaseView(), AnkoLogger, HillfortImageListener {

    lateinit var presenter: HillfortDetailsPresenter
    lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hillfort_details_view_portrait)

        // init presenter
        presenter = initPresenter(HillfortDetailsPresenter(this)) as HillfortDetailsPresenter

        // init toolbar
        init(toolbar, true)

        // init map
        mvLocMap.onCreate(savedInstanceState)
        mvLocMap.getMapAsync{
            map = it
            presenter.doConfigureMap(map)

            map.setOnMapClickListener{
                info("Map has been clicked.")
            }
        }

        // init recycler view
        val test: Int = R.integer.image_columns
        rvHillfortImages.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.image_columns))
        presenter.loadHillforts()
    }

    override fun showHillfort(hillfort: HillfortModel) {
        rvHillfortImages.adapter = HillfortImagesAdapter(hillfort.images, this)
        rvHillfortImages.adapter?.notifyDataSetChanged()

        hillfort.also {
            etName.setText(it.name)
            etDescription.setText(it.desc)
            mltNotes.setText(it.notes)
        }
        showDateVisited(hillfort.dateVisited)   // cannot be called within scope function because it can be null
    }

    fun onVisitedClicked(view: View){
        if (view is CheckBox){
            presenter.doHillfordVisited(cbVisited.isChecked)
        }
        // when textView after actual textBox is clicked the user exspects the same behavior
        if(view.id == R.id.tvVisitedOn){
            cbVisited.isChecked = !cbVisited.isChecked  // checkbox must manually be set in this case
            presenter.doHillfordVisited(cbVisited.isChecked)
        }
    }

    override fun showDateVisited(date: Date?){
        if(date != null){
            tvVisitedOn.text = SimpleDateFormat(dateFormat, Locale.GERMANY).format(date)
            cbVisited.setText(R.string.cb_visited_on)
            cbVisited.isChecked = true
        }
        else{
            cbVisited.setText(R.string.cb_not_yet_visited)
            tvVisitedOn.text = ""
            cbVisited.isChecked = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemAddImage -> {
                presenter.doSelectImage()
            }
            R.id.itemSave -> {
                val name = etName.text.toString()
                val desc = etDescription.text.toString()
                val notes = mltNotes.text.toString()
                val visited = cbVisited.isChecked
                val visitedOn = tvVisitedOn.text.toString()
                var visitedDate: Date? = null
                if(visited){
                    try {
                        visitedDate = SimpleDateFormat(dateFormat, Locale.GERMANY).parse(visitedOn)
                    } catch (e: ParseException){
                        // should never happen as date will always be populated automatically!
                        error("Visited date was populated incorrectly! Error:\n$e")
                        presenter.doAddOrSave(name, desc, notes, null)
                    }
                }
                presenter.doAddOrSave(name, desc, notes, visitedDate)
            }
            R.id.itemCancel -> presenter.doCancel()
            R.id.itemDelete -> presenter.doDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort_details, menu)
        if(presenter.editMode && menu != null){
            menu.getItem(1)?.isVisible = true // show delete Button
            menu.getItem(3)?.setTitle(R.string.b_item_save)  //add button --> save button
        }
        if(presenter.hillfort.images.size < resources.getInteger(R.integer.max_images)) {
            menu?.getItem(0).let {
                it?.isVisible = true  // show add image button
                it?.setShowAsAction(ActionMenuItem.SHOW_AS_ACTION_ALWAYS)   // show image as button not as dropdown
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        info("Hillfort menu")
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onImageClick(image: String, id: Int) {
        info("Image #$id clicked.\nImage path: $image")
        presenter.doSelectImage(image, id)
    }

    override fun onImageLongClick(image: String, index: Int) {
        info("Long Click")
    }

    override fun onDestroy() {
        super.onDestroy()
        mvLocMap.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mvLocMap.onPause()
    }

    override fun onResume() {
        super.onResume()
        mvLocMap.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvLocMap.onLowMemory()
    }

    override fun onBackPressed() {
        presenter.doCancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvLocMap.onSaveInstanceState(outState)
    }
}