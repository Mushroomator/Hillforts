package de.tp.hillforts.views.editLocation

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortFireStore
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.models.location.LocationModel
import de.tp.hillforts.views.BasePresenterFragment
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.toast

class EditLocationFragmentPresenter(var view: EditLocationFragment?): BasePresenterFragment(view) {
    private val LOCATION = "location"

    lateinit var location: LocationModel

    init {
        val args: EditLocationFragmentArgs by view!!.navArgs()
        location = args.location.copy()
    }

    /**
     * Configure google map and display hillfort on screen.
     * @param map map to be configured
     */
    fun doConfigureMap(map: GoogleMap?){
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title(view?.resources?.getString(R.string.hillfort_marker_title))
            .draggable(true)
            .position(loc)
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        view?.showHillfort(HillfortModel(loc = location))
    }

    /**
     * Update hillfort location and show it.
     * @param lat latitude
     * @param lng longitude
     */
    fun doUpdateLocation(lat: Double, lng: Double){
        location.lat = lat
        location.lng = lng
        view?.showHillfort(HillfortModel(loc = location))
    }

    /**
     * Return current location to calling actitiviy.
     */
    fun doSetLocation(){
        val action = EditLocationFragmentDirections.editLocationToDetails(photo = null, hillfort = null, location = location)
        view?.findNavController()?.navigate(action)
    }

    /**
     * Return nothing so location is not updated.
     */
    fun doCancel(){
        val action = EditLocationFragmentDirections.editLocationToDetails(photo = null, hillfort = null, location = null)
        view?.findNavController()?.navigate(action)
    }
}