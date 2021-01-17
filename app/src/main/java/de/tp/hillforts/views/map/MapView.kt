package de.tp.hillforts.views.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.hillfortDetails.HillfortDetailsPresenter
import kotlinx.android.synthetic.main.hillford_list_view_portrait.*
import kotlinx.android.synthetic.main.hillford_list_view_portrait.toolbar
import kotlinx.android.synthetic.main.hillfort_details_view_portrait.*
import kotlinx.android.synthetic.main.map_view_portrait.*

class MapView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: MapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_view_portrait)

        // init presenter
        presenter = initPresenter(MapPresenter(this)) as MapPresenter

        // init toolbar
        init(toolbar, true)

        // init map
        mvAllHillforts.onCreate(savedInstanceState)
        mvAllHillforts.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMarkerClickListener(this)
        }
    }

    override fun showHillfort(hillfort: HillfortModel){
        tvHillfortName.text = hillfort.name
        tvHillfortDescription.text = hillfort.desc
        val image = hillfort.images.first{ it.isNotEmpty() }
        if(image != null){
            Glide.with(this).load(image).into(ivHillfortImageMap);
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true; // false means default behavior = camera zooms on marker on popup will be displayed; true: custom event
    }

    fun onCardClick(view: View){
        presenter.doCardClicked()
    }

    // Lifecycle events
    override fun onDestroy() {
        super.onDestroy()
        mvAllHillforts.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvAllHillforts.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mvAllHillforts.onPause()
    }

    override fun onResume() {
        super.onResume()
        mvAllHillforts.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvAllHillforts.onSaveInstanceState(outState)
    }
}