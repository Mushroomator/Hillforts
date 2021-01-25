package de.tp.hillforts.views.hillfortMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.hillfort_map_view.*
import kotlinx.android.synthetic.main.login_view.*
import kotlinx.android.synthetic.main.login_view.view.*
import org.jetbrains.anko.toast

class HillfortMapFragment: Fragment(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: HillfortMapFragmentPresenter
    lateinit var hostView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hostView = inflater.inflate(R.layout.hillfort_map_view, container, false)
        return hostView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init presenter
        presenter = HillfortMapFragmentPresenter(this)

        // init map
        mvAllHillforts.onCreate(savedInstanceState)
        mvAllHillforts.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMarkerClickListener(this)
        }

        detailsCardView.setOnClickListener { presenter.doCardClicked() }

        super.onViewCreated(view, savedInstanceState)
    }

    fun showHillfort(hillfort: HillfortModel){
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

    // Lifecycle events
    override fun onDestroy() {
        super.onDestroy()
        mvAllHillforts?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvAllHillforts?.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mvAllHillforts?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mvAllHillforts?.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvAllHillforts?.onSaveInstanceState(outState)
    }


}