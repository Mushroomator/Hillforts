package de.tp.hillforts.views.hillfortMap

import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.hillfortDetails.HillfortDetailsView

class HillfortMapPresenter(view: HillfortMapView) : BasePresenter(view) {

    private val HILLFORT_EDIT = "hillfort_edit"
    lateinit var map: GoogleMap
    var currentHillfort: HillfortModel? = null

    init {
        this.view = view
    }

    fun doConfigureMap(map: GoogleMap) {
        this.map = map
        map.uiSettings.setZoomControlsEnabled(true)
        map.uiSettings.setAllGesturesEnabled(true)
        val hillforts = app.hillforts.findAll()
        hillforts.forEach {
            val loc = LatLng(it.loc.lat, it.loc.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag =
                it.fbId  // add the marker on the map and a tag to the marker to be able to identify which marker/ Placemark was clicked on later on
        }
        try {
            val first = hillforts.first()
            currentHillfort = first
            // zoom in to the hillfort
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(first.loc.lat, first.loc.lng),
                    first.loc.zoom
                )
            )
            view?.showHillfort(first)
        } catch (e: NoSuchElementException) {

        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as String
        var current: HillfortModel? = null
        try {
            current = app.hillforts.findAll().find { it.fbId.equals(tag) }
        } catch (e: NoSuchElementException) {

        }
        currentHillfort = current
        if (current != null) {
            view?.showHillfort(current!!)
        }
    }

    fun doCardClicked() {
        if (currentHillfort != null) {
            val intent = Intent(view, HillfortDetailsView::class.java)
            intent.putExtra(HILLFORT_EDIT, currentHillfort)
            view?.startActivity(intent)
        }
    }
}