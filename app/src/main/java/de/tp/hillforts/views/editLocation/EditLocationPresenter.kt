package de.tp.hillforts.views.editLocation

import android.app.Activity
import android.content.Intent
import android.provider.Settings.Global.getString
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.hillforts.R
import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.models.LocationModel
import de.tp.hillforts.views.BasePresenter
import kotlin.math.ln

class EditLocationPresenter(view: EditLocationView): BasePresenter(view) {

    private val LOCATION = "location"

    lateinit var location: LocationModel

    init {
        if(view.intent.hasExtra(LOCATION) && view.intent.extras?.getParcelable<LocationModel>(LOCATION) != null){
            location = view.intent.extras?.getParcelable(LOCATION)!!
        }
    }

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

    fun doUpdateLocation(lat: Double, lng: Double){
        location.lat = lat
        location.lng = lng
        view?.showHillfort(HillfortModel(loc = location))
    }

    fun doOnBackPressed(){
        val resultIntent = Intent()
        resultIntent.putExtra(LOCATION, location)
        view?.setResult(Activity.RESULT_OK, resultIntent)
        view?.finish()
    }


}