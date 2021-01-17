package de.tp.hillforts.views.editLocation

import android.app.Activity
import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.models.location.LocationModel
import de.tp.hillforts.views.BasePresenter

class EditLocationPresenter(view: EditLocationView): BasePresenter(view) {

    private val LOCATION = "location"

    lateinit var location: LocationModel

    init {
        if(view.intent.hasExtra(LOCATION) && view.intent.extras?.getParcelable<LocationModel>(LOCATION) != null){
            location = view.intent.extras?.getParcelable(LOCATION)!!
        }
    }

    /**
     * Configure google map and display hillfort on screen.
     * @param map map to be configured
     */
    fun doConfigureMap(map: GoogleMap){
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title(view?.resources?.getString(R.string.hillfort_marker_title))
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
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
    fun doOnBackPressed(){
        val resultIntent = Intent()
        resultIntent.putExtra(LOCATION, location)
        view?.setResult(Activity.RESULT_OK, resultIntent)
        view?.finish()
    }


}