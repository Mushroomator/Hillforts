package de.tp.hillforts.views.hillfortMap

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.hillfort_list_view.toolbar
import kotlinx.android.synthetic.main.hillfort_map_view.*

class HillfortMapView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: HillfortMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hillfort_map_view)

        // init presenter
        presenter = initPresenter(HillfortMapPresenter(this)) as HillfortMapPresenter

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
        var image: String? = null
        try {
            image = hillfort.images.first{ it.isNotEmpty() }
        } catch (e: NoSuchElementException){

        }
        if(image != null){
            Glide.with(cvHillfortImageWrapper.context).load(image).into(ivHillfortMap)
        }else{
            // show no image if a hillfort does not have an image (otherwise image of a previous hillfort might be displayed!)
            ivHillfortMap.setImageResource(0)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return false; // false means default behavior = camera zooms on marker on popup will be displayed; true: custom event
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