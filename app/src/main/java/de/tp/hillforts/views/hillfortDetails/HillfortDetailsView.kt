package de.tp.hillforts.views.hillfortDetails

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.SearchView
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.forEach
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.GoogleMap
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.VIEW
import kotlinx.android.synthetic.main.hillford_list_view.toolbar
import kotlinx.android.synthetic.main.hillfort_details_view.*
import org.jetbrains.anko.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class HillfortDetailsView : BaseView(), AnkoLogger, HillfortImageListener{

    lateinit var presenter: HillfortDetailsPresenter
    lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hillfort_details_view)

        // init presenter
        presenter = initPresenter(HillfortDetailsPresenter(this)) as HillfortDetailsPresenter

        // init toolbar
        init(toolbar, true)

        // init map
        mvEditLocation.onCreate(savedInstanceState)
        mvEditLocation.getMapAsync {
            map = it
            presenter.doConfigureMap(map)

            map.setOnMapClickListener {
                cacheHillfort()
                presenter.doEditLocation()
            }
        }

        bAddFav.setOnClickListener {
            presenter.doAddOrRemoveFavourites()
        }

        // update cached rating when it is changed
        rbHillfortRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (ratingBar.id == R.id.rbHillfortRating && fromUser){
                presenter.doChangeRating(rating)
            }
        }

        // init recycler view
        rvHillfortImages.layoutManager = GridLayoutManager(
            this,
            resources.getInteger(R.integer.image_columns)
        )
        presenter.loadHillfort()
    }

    override fun showHillfort(hillfort: HillfortModel) {
        rvHillfortImages.adapter = HillfortImagesAdapter(hillfort.images, this)
        rvHillfortImages.adapter?.notifyDataSetChanged()

        hillfort.also {
            etName.setText(it.name)
            etDescription.setText(it.desc)
            mltNotes.setText(it.notes)
            rbHillfortRating.rating = it.rating
            updateLocation(it.loc.lat, it.loc.lng)
            showFavourite(it.isFavourite)
        }
        showDateVisited(hillfort.dateVisited)   // cannot be called within scope function because it can be null
    }

    /**
     * Listener on Checkbox and following TextView. Set visitedOn date when clicked.
     * @param view clicked view
     */
    fun onVisitedClicked(view: View) {
        if (view is CheckBox) {
            presenter.doHillfordVisited(cbVisited.isChecked)
        }
        // when textView after actual textBox is clicked the user exspects the same behavior
        if (view.id == R.id.tvVisitedOn) {
            cbVisited.isChecked =
                !cbVisited.isChecked  // checkbox must manually be set in this case
            presenter.doHillfordVisited(cbVisited.isChecked)
        }
    }

    /**
     * Display visitedOn date
     * @param date visited on
     */
    override fun showDateVisited(date: Date?) {
        if (date != null) {
            tvVisitedOn.text = SimpleDateFormat(dateFormat, Locale.GERMANY).format(date)
            cbVisited.setText(R.string.cb_visited_on)
            cbVisited.isChecked = true
        } else {
            cbVisited.setText(R.string.cb_not_yet_visited)
            tvVisitedOn.text = ""
            cbVisited.isChecked = false
        }
    }

    /**
     * Toggle "Add to favourite" Button and change button style accordingly
     * @param isFavourite true if hillfort is marked as favourite
     * @author Thomas Pilz
     */
    fun showFavourite(isFavourite: Boolean) {
        if (isFavourite) {
            bAddFav.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_favorite_24,
                0,
                0,
                0
            )
            bAddFav.setBackgroundColor(getColor(R.color.red))
            bAddFav.setTextColor(getColor(R.color.white))
            bAddFav.compoundDrawableTintList = ColorStateList.valueOf(getColor(R.color.white))
            bAddFav.text = getString(R.string.b_remove_fav)

        } else {
            bAddFav.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_favorite_border_24,
                0,
                0,
                0
            )
            bAddFav.setBackgroundColor(getColor(R.color.white))
            bAddFav.setTextColor(getColor(R.color.red))
            bAddFav.compoundDrawableTintList = ColorStateList.valueOf(getColor(R.color.red))
            bAddFav.text = getString(R.string.b_add_fav)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemTakePhoto -> {
                presenter.doTakePhoto()
            }
            R.id.itemAddImage -> {
                cacheHillfort()
                presenter.doSelectImage(IMAGEOPTION.GALLERY_IMAGE)
                invalidateOptionsMenu() //invalidate options menu so onPrepareOptionsMenu will be called after
            }
            R.id.itemSave -> {
                val name = etName.text.toString()
                if (name == "" || name.isEmpty()) {
                    toast(getString(R.string.toast_details_invalid_input))
                    return false
                }
                val desc = etDescription.text.toString()
                val notes = mltNotes.text.toString()
                val visited = cbVisited.isChecked
                val visitedOn = tvVisitedOn.text.toString()
                var visitedDate: Date? = null
                val rating = rbHillfortRating.rating
                if (visited) {
                    try {
                        visitedDate = SimpleDateFormat(dateFormat, Locale.GERMANY).parse(visitedOn)
                    } catch (e: ParseException) {
                        // should never happen as date will always be populated automatically!
                        error("Visited date was populated incorrectly! Error:\n$e")
                        return false
                    }
                }
                presenter.doAddOrSave(name, desc, notes, visitedDate, rating)
            }
            R.id.itemShare -> {
                val name = etName.text.toString()
                if (name == "" || name.isEmpty()) {
                    toast(getString(R.string.toast_details_invalid_input))
                    return false
                }
                presenter.doShare()
            }
            R.id.itemCancel -> presenter.doCancel()
            R.id.itemDelete -> presenter.doDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort_details, menu)
        if (menu != null && presenter.editMode) {
            menu.forEach {
                when(it.itemId){
                    R.id.itemDelete -> it.isVisible = true  // show delete Button
                    R.id.itemSave -> it.setTitle(R.string.b_item_save)  //add button --> save button
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Called every time before menu is displayed.
     * Invalidate menu as users might want to change images and then abort that action again.
     */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            if (presenter.hillfort.images.size < resources.getInteger(R.integer.max_images)) {
                invalidateOptionsMenu() // if back is pressed --> no picture added --> need to recalculate number of images
                val menuAddImage = menu.findItem(R.id.itemAddImage)
                menuAddImage.also {
                    it?.isVisible = true  // show add image button
                    it?.setShowAsAction(ActionMenuItem.SHOW_AS_ACTION_ALWAYS)   // show image as button not as dropdown
                }
                val menuTakePhoto = menu.findItem(R.id.itemTakePhoto)
                menuTakePhoto.also {
                    it?.isVisible = true  // show add image button
                    it?.setShowAsAction(ActionMenuItem.SHOW_AS_ACTION_IF_ROOM)   // show image as button not as dropdown
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
    override fun updateLocation(lat: Double, lng: Double) {
        tvLatValDetails.text = "%.6f".format(lat)
        tvLngValDetails.text = "%.6f".format(lng)
    }

    /**
     * Cache values of text views so they dont get lost when calling another activity.
     */
    fun cacheHillfort() {
        presenter.doCacheHillford(
            etName.text.toString(),
            etDescription.text.toString(),
            mltNotes.text.toString()
        )
    }

    /**
     * Listener on image clicks. When image is clicked allow user to select a different image.
     */
    override fun onImageClick(image: String, index: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.modal_change_image))
        builder.setMessage(R.string.modal_change_image_text)
        builder.setPositiveButton(getString(R.string.modal_change_image_gallery),
            { dialog, id ->
                    // User cancelled the dialog
                    presenter.doSelectImage(IMAGEOPTION.GALLERY_IMAGE, image, index)
            })
        builder.setNegativeButton(R.string.modal_change_image_camera,
            { dialog, id ->
                presenter.doSelectImage(IMAGEOPTION.CAMERA_PHOTO, image, index)
            })
        builder.setNeutralButton(getString(R.string.modal_cancel),
            { dialog, id ->
                /* no-op */
            })
        builder.create()
        builder.show()
    }

    /**
     * Show menu of different actions to perform on menu.
     */
    override fun onImageLongClick(image: String, index: Int) {
        info("Long Click")
    }

    override fun onStart() {
        super.onStart()
        mvEditLocation.onStart()
    }

    override fun onStop() {
        super.onStop()
        mvEditLocation.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        mvEditLocation.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState, outPersistentState)
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
        presenter.loadHillfort()
        presenter.doResartLocationUpdates() //restart location updates
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