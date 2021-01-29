package de.tp.hillforts.views.hillfortDetails

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.helpers.checkLocationPermissions
import de.tp.hillforts.helpers.createDefaultLocationRequest
import de.tp.hillforts.helpers.isPermissionGranted
import de.tp.hillforts.helpers.showImagePicker
import de.tp.hillforts.models.hillfort.HillfortFireStore
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.models.location.LocationModel
import de.tp.hillforts.views.BasePresenterFragment
import de.tp.hillforts.views.VIEW
import de.tp.hillforts.views.camera.CameraFragmentDirections
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue


enum class IMAGEOPTION {
    CAMERA_PHOTO,
    GALLERY_IMAGE,
}

class HillfortDetailsFragmentPresenter(var view: HillfortDetailsFragment?) :
    BasePresenterFragment(view) {

    private val HILLFORT_EDIT = "hillfort_edit"
    private val LOCATION_EDIT = "location"
    private val TAKE_PHOTO = "taken_photo"
    private val IMAGE_REQ_ID = 1
    private val LOCATION_REQ_ID = 2
    private val TAKE_PHOTO_REQ_ID = 3

    val dateFormat = "dd/MM/yyyy"

    var map: GoogleMap? = null
    var hillfort = HillfortModel()
    var editMode = false

    // Location
    var defaultLocation = LocationModel(49.141018, 11.854860, 15f)
    var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view!!.requireActivity())
    val locationRequest = createDefaultLocationRequest()
    var locationManuallyChanged = true  // change behavior: assume location is set except it was not InvocationTargetException

    // Current location
    var userCurrentLocation = LocationModel(defaultLocation.lat, defaultLocation.lng)

    init {

        // Check hillfort here
        try {

            val args: HillfortDetailsFragmentArgs by view!!.navArgs()
            if (args.hillfort != null) {
                editMode = true
                hillfort = args.hillfort!!
            }

            // check if photo was taken
            if (args.photo != null) {
                val photo = args.photo!!
                getCacheIfAvailable()
                if (app.cachePreviousImage != null) {
                    // a new photo has been made --> cant be "equal to previous"
                    hillfort.images[app.cachePreviousImage!!] = photo
                    app.cachePreviousImage = null // invalidate cache
                } else {
                    // add image
                    hillfort.images.add(photo)
                }
            }

            // check if location was changed
            if (args.location != null) {
                val location = args.location!!
                getCacheIfAvailable()
                hillfort.loc = location
            }
        } catch (e: InvocationTargetException) {
            // only thrown when there are no arguments given --> new hillfort --> get location
            locationManuallyChanged = false
            if(checkLocationPermissions(view!!.requireActivity())){
                doSetCurrentLocation()
            }
        }
        // showHillfort() will be called at the end of OnViewCreated() in Fragement!
    }

    fun getCacheIfAvailable(){
        if(app.hillfortCache != null){
            hillfort = app.hillfortCache!!
            app.hillfortCache = null
        }
    }

    /**
     * Update location marker on map and labels on screen.
     * @param lat latitude
     * @param lng longitude
     */
    fun locationUpdate(lat: Double, lng: Double) {
        hillfort.loc.lat = lat
        hillfort.loc.lng = lng
        map?.clear()
        Log.i(TAG, "Location changed: $lat, $lng")
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.name)
            .position(LatLng(hillfort.loc.lat, hillfort.loc.lng))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    hillfort.loc.lat,
                    hillfort.loc.lng
                ), hillfort.loc.zoom
            )
        )
        view?.updateLocation(hillfort.loc.lat, hillfort.loc.lng)
    }

    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            // permissions denied, so use the default location
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }

    /**
     * Set the the hillfort location
     */
    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
            userCurrentLocation.lat = it.latitude
            userCurrentLocation.lng = it.longitude
        }
    }

    fun doOnDestroy() {
        view = null
    }


    /**
     * Request another location update if not in edit mode.
     */
    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if (!locationManuallyChanged) {
                        locationUpdate(l.latitude, l.longitude)
                    }
                    userCurrentLocation.lat = l.latitude
                    userCurrentLocation.lng = l.longitude

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
        locationUpdate(hillfort.loc.lat, hillfort.loc.lng)
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
        // load from cache if needed and invalidate cache
        if (app.hillfortCache != null) {
            hillfort = app.hillfortCache!!
            app.hillfortCache = null
        }
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
        app.hillfortCache = hillfort
    }

    /**
     * Show image picker to add/ replace an image.
     * @param image previous image
     * @param index index of previous image
     */
    fun doSelectImage(option: IMAGEOPTION, image: String? = null, index: Int? = null) {
        view?.also {
            if (image != null && index != null) {
                app.cachePreviousImage = index
            } else {
                app.cachePreviousImage = null
            }
            when (option) {
                IMAGEOPTION.GALLERY_IMAGE -> showImagePicker(view!!, IMAGE_REQ_ID)
                IMAGEOPTION.CAMERA_PHOTO -> doTakePhoto()
            }
        }
    }

    /**
     * Call activity to set hilfort location.
     */
    fun doEditLocation() {
        locationManuallyChanged = true
        val action = HillfortDetailsFragmentDirections.detailsToEditLocation(hillfort.loc)
        view?.findNavController()?.navigate(action)
    }

    fun doChangeRating(rating: Float) {
        hillfort.rating = rating
    }

    fun doNavigateToHillfort() {
        val precision: Double = 0.001
        val latOff = (userCurrentLocation.lat - hillfort.loc.lat).absoluteValue
        val lngOff = (userCurrentLocation.lng - hillfort.loc.lng).absoluteValue
        if (latOff < precision && lngOff < precision) {
            // location difference is so small --> considered same location (Google wont find a route since there really is no route!)
            Toast.makeText(
                view?.context,
                view?.getString(R.string.toast_same_location),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val gmmIntentUri = Uri.parse("google.navigation:q=${hillfort.loc.lat},${hillfort.loc.lng}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        view?.startActivity(mapIntent)
    }

    fun doOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            when (requestCode) {
                IMAGE_REQ_ID -> {
                    val newImgPath = data.data.toString()
                    if (app.cachePreviousImage != null) {
                        // attempt to change image
                        if (!newImgPath.equals(hillfort.images[app.cachePreviousImage!!])) {
                            hillfort.images[app.cachePreviousImage!!] = newImgPath
                            app.cachePreviousImage = null // invalidate cache
                        }
                    } else {
                        // add image
                        hillfort.images.add(newImgPath)
                    }
                    view?.showHillfort(hillfort)
                }
                /*
                LOCATION_REQ_ID -> {
                    if (data.extras?.getParcelable<LocationModel>(LOCATION_EDIT) != null) {
                        hillfort.loc = data.extras?.getParcelable(LOCATION_EDIT)!!
                        locationUpdate(hillfort.loc.lat, hillfort.loc.lng)
                        view?.showHillfort(hillfort)
                    }
                }

                TAKE_PHOTO_REQ_ID -> {
                    // get path of taken image
                    if (data.hasExtra(TAKE_PHOTO)) {
                        val photo = data.getStringExtra(TAKE_PHOTO)!!
                        if (previousImage != null) {
                            // a new photo has been made --> cant be "equal to previous"
                            hillfort.images[previousImage!!] = photo
                        } else {
                            // add image
                            hillfort.images.add(photo)
                        }
                        view.showHillfort(hillfort)
                    }
                }

                 */
            }
        }
    }

    /**
     * Toggle isFavourite and show result on screen.
     */
    fun doAddOrRemoveFavourites() {
        hillfort.isFavourite = !hillfort.isFavourite
        view?.showFavourite(hillfort.isFavourite)
    }

    fun doShare() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            hillfort.also {
                var dateStr = ""
                if (it.dateVisited != null) {
                    dateStr = SimpleDateFormat(dateFormat, Locale.GERMANY).format(it.dateVisited!!)
                } else {
                    dateStr = "Not yet visited"
                }
                putExtra(
                    Intent.EXTRA_TEXT, "Hey there,\ntake a look at one of my hillforts:" +
                            "\nName: ${it.name}" +
                            "\nDescription: ${if (it.desc != "") it.desc else "No description"}" +
                            "\nNotes: ${if (it.notes != "") it.notes else "No notes"}" +
                            "\nVisited: $dateStr" +
                            "\nRating: ${"%.1f".format(it.rating)}"
                )
                var uriList = ArrayList<Uri>()
                hillfort.images.forEach { image -> uriList.add(Uri.parse(image)) }
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
            }
            type = "image/*"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Hillfort via...")
        view?.startActivity(shareIntent)
    }

    fun doTakePhoto() {
        val navController = view?.findNavController()
        navController?.navigate(R.id.cameraFragment)
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
        val navController = view?.findNavController()
        navController!!.navigate(R.id.hillfortListFragment)
    }

    /**
     * Cancel operation. Do nothing.
     */
    fun doCancel() {
        val navController = view?.findNavController()
        navController?.navigate(R.id.hillfortListFragment)
    }

    /**
     * Delete hillford.
     */
    fun doDelete() {
        val navController = view?.findNavController()
        app.hillforts.delete(hillfort)
        navController?.navigate(R.id.hillfortListFragment)
    }

}