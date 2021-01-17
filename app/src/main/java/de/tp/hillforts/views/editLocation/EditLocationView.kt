package de.tp.hillforts.views.editLocation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.edit_location_view_portrait.*
import kotlinx.android.synthetic.main.hillford_list_view_portrait.toolbar

class EditLocationView: BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener{

    lateinit var presenter: EditLocationPresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_location_view_portrait)

        // init presenter
        presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter

        // init toolbar
        init(toolbar, true)

        // init map
        mvEditLocationMap.onCreate(savedInstanceState)
        mvEditLocationMap.getMapAsync{
            map = it
            map.setOnMarkerDragListener(this)
            map.setOnMarkerClickListener(this)
            presenter.doConfigureMap(map)
        }
    }

    /**
     * Show hillfort i.e. its location on map.
     * @param hillfort hillfort to be displayed.
     */
    override fun showHillfort(hillfort: HillfortModel) {
        tvLatValEL.text = "%.6f".format(hillfort.loc.lat)
        tvLngValEL.text = "%.6f".format(hillfort.loc.lng)
    }

    /**
     * Show snippet of marker.
     * @param marker location marker
     */
    fun doShowMarkerSnippet(marker: Marker) {
        val loc = LatLng(marker.position.latitude, marker.position.longitude)
        marker.snippet = "Lat: ${"%.6f".format(loc.latitude)}   Lng: ${"%.6f".format(loc.longitude)}"
    }

    /**
     * Listener on marker clicks. Displays snippet.
     * @param marker clicked marker
     */
    override fun onMarkerClick(marker: Marker): Boolean {
        doShowMarkerSnippet(marker)
        return false
    }

    override fun onMarkerDragStart(marker: Marker?) {

    }

    /**
     * Called when marker is dragged. Updates location labels.
     * @param marker dragged marker
     */
    override fun onMarkerDrag(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
    }

    override fun onMarkerDragEnd(marker: Marker?) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_location_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemCancel -> finish()
            R.id.itemSave -> {
                presenter.doOnBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        mvEditLocationMap.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mvEditLocationMap.onPause()
    }

    override fun onResume() {
        super.onResume()
        mvEditLocationMap.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvEditLocationMap.onLowMemory()
    }
}