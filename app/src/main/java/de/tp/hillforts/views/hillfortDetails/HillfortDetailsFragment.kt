package de.tp.hillforts.views.hillfortDetails

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.GoogleMap
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import kotlinx.android.synthetic.main.hillfort_details_view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HillfortDetailsFragment: Fragment(), HillfortImageListener {

    lateinit var presenter: HillfortDetailsFragmentPresenter
    lateinit var hostView: View
    lateinit var map: GoogleMap
    val dateFormat = "dd/MM/yyyy"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hostView = inflater.inflate(R.layout.hillfort_details_view, container, false)

        setHasOptionsMenu(true)

        return hostView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init presenter
        presenter = HillfortDetailsFragmentPresenter(this)

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

        // favourite button
        bAddFav.setOnClickListener {
            presenter.doAddOrRemoveFavourites()
        }

        // update cached rating when it is changed
        rbHillfortRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (ratingBar.id == R.id.rbHillfortRating && fromUser){
                presenter.doChangeRating(rating)
            }
        }

        cbVisited.setOnClickListener {
            visitedClicked(it)
        }

        tvVisitedOn.setOnClickListener {
            visitedClicked(it)
        }

        // init recycler view
        rvHillfortImages.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.image_columns))
        presenter.loadHillfort()

        super.onViewCreated(view, savedInstanceState)
    }

    fun showHillfort(hillfort: HillfortModel) {
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

    override fun onAttach(context: Context) {
        /*
         if(app.hillfortCache != null){
            editMode = true
            hillfort = app.hillfortCache!!
            app.hillfortCache = null // invalidate cache
        }

         */
        super.onAttach(context)
    }

    fun visitedClicked(view: View){
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
    fun showDateVisited(date: Date?) {
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
            bAddFav.setBackgroundColor(requireActivity().getColor(R.color.red))
            bAddFav.setTextColor(requireActivity().getColor(R.color.white))
            bAddFav.compoundDrawableTintList = ColorStateList.valueOf(requireActivity().getColor(R.color.white))
            bAddFav.text = getString(R.string.b_remove_fav)

        } else {
            bAddFav.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_favorite_border_24,
                0,
                0,
                0
            )
            bAddFav.setBackgroundColor(requireActivity().getColor(R.color.white))
            bAddFav.setTextColor(requireActivity().getColor(R.color.red))
            bAddFav.compoundDrawableTintList = ColorStateList.valueOf(requireActivity().getColor(R.color.red))
            bAddFav.text = getString(R.string.b_add_fav)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemNavigate -> {
                cacheHillfort()
                presenter.doNavigateToHillfort()
            }
            R.id.itemTakePhoto -> {
                cacheHillfort()
                presenter.doTakePhoto()
            }
            R.id.itemAddImage -> {
                cacheHillfort()
                presenter.doSelectImage(IMAGEOPTION.GALLERY_IMAGE)
                activity?.invalidateOptionsMenu() //invalidate options menu so onPrepareOptionsMenu will be called after
            }
            R.id.itemSave -> {
                val name = etName.text.toString()
                if (name == "" || name.isEmpty()) {
                    Toast.makeText(context, getString(R.string.toast_details_invalid_input), Toast.LENGTH_LONG).show()
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
                        Log.e(TAG, "Visited date was populated incorrectly! Error:\n$e")
                        return false
                    }
                }
                presenter.doAddOrSave(name, desc, notes, visitedDate, rating)
            }
            R.id.itemShare -> {
                cacheHillfort()
                val name = etName.text.toString()
                if (name == "" || name.isEmpty()) {
                    Toast.makeText(context, getString(R.string.toast_details_invalid_input), Toast.LENGTH_LONG).show()
                    return false
                }
                presenter.doShare()
            }
            R.id.itemCancel -> presenter.doCancel()
            R.id.itemDelete -> presenter.doDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.doOnActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_hillfort_details, menu)
        if (menu != null && presenter.editMode) {
            menu.forEach {
                when(it.itemId){
                    R.id.itemDelete -> it.isVisible = true  // show delete Button
                    R.id.itemSave -> it.setTitle(R.string.b_item_save)  //add button --> save button
                }
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Called every time before menu is displayed.
     * Invalidate menu as users might want to change images and then abort that action again.
     */
    override fun onPrepareOptionsMenu(menu: Menu) {
        // do not show images recycler view and fill whole screen in landscape mode
        if(presenter.hillfort.images.size == 0){
            rvHillfortImages.visibility = View.GONE
            if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                guideLineMiddle.setGuidelinePercent(1f)
            }
        }
        else{
            rvHillfortImages.visibility = View.VISIBLE
            if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                guideLineMiddle.setGuidelinePercent(0.5f)
            }
        }

        if (menu != null) {
            if (presenter.hillfort.images.size < resources.getInteger(R.integer.max_images)) {
                activity?.invalidateOptionsMenu() // if back is pressed --> no picture added --> need to recalculate number of images
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
        super.onPrepareOptionsMenu(menu)
    }

    /**
     * Update hillfort location displayed.
     * @param lat latitude
     * @param lng longitude
     */
    fun updateLocation(lat: Double, lng: Double) {
        tvLatValDetails?.text = "%.6f".format(lat)
        tvLngValDetails?.text = "%.6f".format(lng)
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
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.modal_change_image))
        builder.setMessage(R.string.modal_change_image_text)
        builder.setPositiveButton(getString(R.string.modal_change_image_gallery),
            { _ , _ ->
                // User cancelled the dialog
                cacheHillfort()
                presenter.doSelectImage(IMAGEOPTION.GALLERY_IMAGE, image, index)
            })
        builder.setNegativeButton(R.string.modal_change_image_camera,
            { _ , _ ->
                cacheHillfort()
                presenter.doSelectImage(IMAGEOPTION.CAMERA_PHOTO, image, index)
            })
        builder.setNeutralButton(getString(R.string.modal_cancel),
            { _ , _ ->
                /* no-op */
            })
        builder.create()
        builder.show()
    }

    /**
     * Show menu of different actions to perform on menu.
     */
    override fun onImageLongClick(image: String, index: Int) {

    }

    override fun onStart() {
        super.onStart()
        mvEditLocation?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mvEditLocation?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.doOnDestroy()
        mvEditLocation?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mvEditLocation?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mvEditLocation?.onResume()
        presenter.loadHillfort()
        presenter.doResartLocationUpdates() //restart location updates
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvEditLocation?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvEditLocation?.onSaveInstanceState(outState)
    }
}