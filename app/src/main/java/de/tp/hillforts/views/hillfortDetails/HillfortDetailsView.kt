package de.tp.hillforts.views.hillfortDetails

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
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
import org.jetbrains.anko.toast
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
        mvEditLocation.onCreate(savedInstanceState)
        mvEditLocation.getMapAsync{
            map = it
            presenter.doConfigureMap(map)

            map.setOnMapClickListener{
                cacheHillfort()
                presenter.doEditLocation()
            }
        }

        // init recycler view
        rvHillfortImages.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.image_columns))
        presenter.loadHillfort()
    }

    override fun showHillfort(hillfort: HillfortModel) {
        rvHillfortImages.adapter = HillfortImagesAdapter(hillfort.images, this)
        rvHillfortImages.adapter?.notifyDataSetChanged()

        hillfort.also {
            etName.setText(it.name)
            etDescription.setText(it.desc)
            mltNotes.setText(it.notes)
            updateLocation(it.loc.lat, it.loc.lng)
        }
        showDateVisited(hillfort.dateVisited)   // cannot be called within scope function because it can be null
    }

    /**
     * Listener on Checkbox and following TextView. Set visitedOn date when clicked.
     * @param view clicked view
     */
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

    /**
     * Display visitedOn date
     * @param date visited on
     */
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
                cacheHillfort()
                presenter.doSelectImage()
                invalidateOptionsMenu() //invalidate options menu so onPrepareOptionsMenu will be called after
            }
            R.id.itemSave -> {
                val name = etName.text.toString()
                if(name == "" || name.isEmpty()){
                    toast(getString(R.string.toast_details_invalid_input))
                    return false
                }
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
                        return false
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
        if(menu != null){
            if(presenter.editMode) {
                menu.getItem(1)?.isVisible = true // show delete Button
                menu.getItem(3)?.setTitle(R.string.b_item_save)  //add button --> save button
            }

        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Called every time before menu is displayed.
     * Invalidate menu as users might want to change images and then abort that action again.
     */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(menu != null){
            if(presenter.hillfort.images.size < resources.getInteger(R.integer.max_images)) {
                menu.getItem(0).also {
                    invalidateOptionsMenu() // if back is pressed --> no picture added --> need to recalculate number of images
                    it?.isVisible = true  // show add image button
                    it?.setShowAsAction(ActionMenuItem.SHOW_AS_ACTION_ALWAYS)   // show image as button not as dropdown
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    /**
     * Update hillfort location displayed.
     * @param lat latitude
     * @param lng longitude
     */
    override fun updateLocation(lat: Double, lng: Double){
        tvLatValDetails.text = "%.6f".format(lat)
        tvLngValDetails.text = "%.6f".format(lng)
    }

    /**
     * Cache values of text views so they dont get lost when calling another activity.
     */
    fun cacheHillfort(){
        presenter.doCacheHillford(etName.text.toString(), etDescription.text.toString(), mltNotes.text.toString())
    }

    /**
     * Listener on image clicks. When image is clicked allow user to select a different image.
     */
    override fun onImageClick(image: String, index: Int) {
        info("Image #$index clicked.\nImage path: $image")
        presenter.doSelectImage(image, index)
    }

    /**
     * Show menu of different actions to perform on menu.
     */
    override fun onImageLongClick(image: String, index: Int) {
        info("Long Click")
    }

    override fun onDestroy() {
        super.onDestroy()
        mvEditLocation.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mvEditLocation.onPause()
    }

    override fun onResume() {
        super.onResume()
        mvEditLocation.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvEditLocation.onLowMemory()
    }

    override fun onBackPressed() {
        presenter.doCancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvEditLocation.onSaveInstanceState(outState)
    }
}