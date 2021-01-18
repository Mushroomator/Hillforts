package de.tp.hillforts.views.hillfortDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.hillforts.helpers.checkLocationPermissions
import de.tp.hillforts.helpers.createDefaultLocationRequest
import de.tp.hillforts.helpers.isPermissionGranted
import de.tp.hillforts.helpers.showImagePicker
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.models.location.LocationModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.info
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HillfortDetailsPresenter(view: HillfortDetailsView) : BasePresenter(view) {

    private val HILLFORT_EDIT = "hillfort_edit"
    private val LOCATION_EDIT = "location"
    private val IMAGE_REQ_ID = 1
    private val LOCATION_REQ_ID = 2

    var previousImage: Int? = null
    var map: GoogleMap? = null
    var hillfort = HillfortModel()
    var editMode = false

    // Location
    var defaultLocation = LocationModel(49.141018, 11.854860, 15f)
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var locationManuallyChanged = false

    init {
        if (view.intent.hasExtra(HILLFORT_EDIT) && view.intent.extras?.getParcelable<HillfortModel>(HILLFORT_EDIT) != null) {
            editMode = true
            hillfort =
                view.intent.extras?.getParcelable(HILLFORT_EDIT)!!    //smart cast not possible
            view.showHillfort(hillfort)
        }
        else{
            if(checkLocationPermissions(view)){
                doSetCurrentLocation()
            }
        }
    }

    fun locationUpdate(lat: Double, lng: Double) {
        hillfort.loc.lat = lat
        hillfort.loc.lng = lng
        map?.clear()
        view?.info("Location changed: $lat, $lng")
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.name).position(LatLng(hillfort.loc.lat, hillfort.loc.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.loc.lat, hillfort.loc.lng), hillfort.loc.zoom))
        view?.updateLocation(hillfort.loc.lat, hillfort.loc.lng)
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            // permissions denied, so use the default location
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }


    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if(!locationManuallyChanged){
                        locationUpdate(l.latitude, l.longitude)
                    }
                }
            }
        }
        if (!editMode) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }


    /**
     * Configure Google Map.
     * @param map map to be configured
     */
    fun doConfigureMap(map: GoogleMap) {
        this.map = map
        if (hillfort.loc.zoom == 0f) {
            hillfort.loc.lat = 49.141018
            hillfort.loc.lng = 11.854860
            hillfort.loc.zoom = 15f
        }
        this.map = map
        doUpdateMapLocation()
    }

    /**
     * Set visitedOn.
     * @param visited true if hillfort is visited
     */
    fun doHillfordVisited(visited: Boolean) {
        if (visited) {
            hillfort.dateVisited = Date()
        } else {
            hillfort.dateVisited = null
        }
        view?.showDateVisited(hillfort.dateVisited)
    }

    /**
     * Load hillfort and display it.
     */
    fun loadHillfort() {
        view?.showHillfort(hillfort)
    }

    /**
     * Cache current state of hillford.
     * @param name name for hillford
     * @param desc description for hillford
     * @param notes notes for hillford
     */
    fun doCacheHillford(name: String, desc: String, notes: String) {
        hillfort.name = name
        hillfort.desc = desc
        hillfort.notes = notes
    }

    /**
     * Show image picker to add/ replace an image.
     * @param image previous image
     * @param index index of previous image
     */
    fun doSelectImage(image: String? = null, index: Int? = null) {
        view?.also {
            if (image != null && index != null) {
                previousImage = index
            } else {
                previousImage = null
            }
            showImagePicker(view!!, IMAGE_REQ_ID)
        }
    }

    /**
     * Call activity to set hilfort location.
     */
    fun doEditLocation() {
        locationManuallyChanged = true
        view?.navigateTo(VIEW.EDIT_LOCATION, LOCATION_REQ_ID, LOCATION_EDIT, hillfort.loc)
    }

    /**
     * Update GoogleMap and labels according to hillfort location.
     */
    fun doUpdateMapLocation() {
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        map?.uiSettings?.setAllGesturesEnabled(true)
        val pos = LatLng(hillfort.loc.lat, hillfort.loc.lng)
        val options = MarkerOptions()
            .title(hillfort.name)
            .position(pos)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, hillfort.loc.zoom))
        view?.updateLocation(hillfort.loc.lat, hillfort.loc.lng)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQ_ID -> {
                val newImgPath = data.data.toString()
                if (previousImage != null) {
                    // attempt to change image
                    if (!newImgPath.equals(hillfort.images[previousImage!!])) {
                        hillfort.images[previousImage!!] = newImgPath
                    }
                } else {
                    // add image
                    hillfort.images.add(newImgPath)
                }
                view?.showHillfort(hillfort)
            }
            LOCATION_REQ_ID -> {
                if (data.extras?.getParcelable<LocationModel>(LOCATION_EDIT) != null) {
                    hillfort.loc = data.extras?.getParcelable(LOCATION_EDIT)!!
                    doUpdateMapLocation()
                    view?.showHillfort(hillfort)
                }
            }
        }
    }

    /**
     * Toggle isFavourite and show result on screen.
     */
    fun doAddOrRemoveFavourites(){
        hillfort.isFavourite = !hillfort.isFavourite
        (view as HillfortDetailsView)?.showFavourite(hillfort.isFavourite)
    }

    fun doShare(){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            hillfort.also {
                var dateStr = ""
                if(it.dateVisited != null){
                    dateStr = SimpleDateFormat(view?.dateFormat, Locale.GERMANY).format(it.dateVisited!!)
                }
                else{
                    dateStr = "Not yet visited."
                }
                putExtra(Intent.EXTRA_TEXT, "Hey there,\ntake a look at one of my hillfort:" +
                        "\nName: ${it.name}" +
                        "\nDescription: ${it.desc}" +
                        "\nNotes: ${it.notes}" +
                        "\nVisited: $dateStr" +
                        "\nVisited: $dateStr" +
                        "\nRating: ${"%.1f".format(it.rating)}")
                var uriList = ArrayList<Uri>()
                hillfort.images.forEach{ uriList.add(Uri.parse(it)) }
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
            }
            type = "*/*"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Hillfort via...")
        view?.startActivity(shareIntent)
    }

    /**
     * Update hillfort when in edit-mode or create a new one.
     * @param name name for hillfort
     * @param desc description for hillfort
     * @param notes notes on hillfort
     * @param visitedOn date when hillfort was visited
     */
    fun doAddOrSave(name: String, desc: String, notes: String, visitedOn: Date?, rating: Float) {
        hillfort.also {
            it.name = name
            it.desc = desc
            it.notes = notes
            it.dateVisited = visitedOn
            it.rating = rating
        }
        if (editMode) {
            app.hillforts.update(hillfort)
        } else {
            app.hillforts.create(hillfort)
        }
        view?.finish()
    }

    /**
     * Cancel operation. Do nothing.
     */
    fun doCancel() {
        view?.finish()
    }

    /**
     * Delete hillford.
     */
    fun doDelete() {
        app.hillforts.delete(hillfort)
        view?.finish()
    }
}