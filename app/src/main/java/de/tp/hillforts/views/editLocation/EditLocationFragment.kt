package de.tp.hillforts.views.editLocation

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import kotlinx.android.synthetic.main.edit_location_view.*

class EditLocationFragment: Fragment(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    lateinit var presenter: EditLocationFragmentPresenter
    lateinit var hostView: View
    lateinit var map: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hostView = inflater.inflate(R.layout.edit_location_view, container, false)

        setHasOptionsMenu(true)

        return hostView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init presenter
        presenter = EditLocationFragmentPresenter(this)

        // add custom behavior to save location when back is pressed!
        requireActivity().onBackPressedDispatcher.addCallback {
            presenter.doCancel()    // dont set location on back! User must confirm to change the location
        }

        // init map
        mvEditLocationMap.onCreate(savedInstanceState)
        mvEditLocationMap.getMapAsync{
            map = it
            map.setOnMarkerDragListener(this)
            map.setOnMarkerClickListener(this)
            presenter.doConfigureMap(map)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Show hillfort i.e. its location on map.
     * @param hillfort hillfort to be displayed.
     */
    fun showHillfort(hillfort: HillfortModel) {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_location, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemCancel -> presenter.doCancel()
            R.id.itemSave -> presenter.doSetLocation()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        mvEditLocationMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mvEditLocationMap?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mvEditLocationMap.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mvEditLocationMap?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mvEditLocationMap?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mvEditLocationMap?.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvEditLocationMap?.onLowMemory()
    }

}