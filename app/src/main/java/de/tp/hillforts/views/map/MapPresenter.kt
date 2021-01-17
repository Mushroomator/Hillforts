package de.tp.hillforts.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.hillforts.views.BasePresenter

class MapPresenter(view: MapView): BasePresenter(view) {

    lateinit var map: GoogleMap

    init{
        this.view = view
    }

    fun doConfigureMap(map: GoogleMap){
        this.map = map
        map.uiSettings.setZoomControlsEnabled(true)
        map.uiSettings.setAllGesturesEnabled(true)
        val hillforts = app.hillforts.findAll()
        hillforts.forEach{
            val loc = LatLng(it.loc.lat, it.loc.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it.id  // add the marker on the map and a tag to the marker to be able to identify which marker/ Placemark was clicked on later on
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.loc.zoom)) // zoom in to the placemark (should actually be refatored to be out of iteration as it only needs to be done once as only one placemark can be in focus)
        }
    }

    fun doMarkerSelected(marker: Marker){
        val tag = marker.tag as Long
        val current = app.hillforts.findById(tag)
        if(current != null){
            view?.showHillfort(current)
        }
    }
}