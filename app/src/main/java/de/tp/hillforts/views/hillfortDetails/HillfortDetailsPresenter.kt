package de.tp.hillforts.views.hillfortDetails

import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.hillforts.helpers.showImagePicker
import de.tp.hillforts.models.HillfortModel
import de.tp.hillforts.views.BasePresenter
import org.jetbrains.anko.info
import java.util.*

class HillfortDetailsPresenter(view: HillfortDetailsView): BasePresenter(view) {

    private val HILLFORT_EDIT = "hillfort_edit"
    private val IMAGE_REQ_ID = 1
    var previousImage: Int? = null

    var hillfort = HillfortModel()
    var editMode = false

    init {
        if(view.intent.hasExtra(HILLFORT_EDIT) && view.intent.extras?.getParcelable<HillfortModel>(HILLFORT_EDIT) != null){
            editMode = true
            hillfort = view.intent.extras?.getParcelable(HILLFORT_EDIT)!!    //smart cast not possible
            view.showHillfort(hillfort)
        }
    }

    fun doConfigureMap(map: GoogleMap){
        if (hillfort.loc.zoom == 0f) {
            hillfort.loc.lat = 49.141018
            hillfort.loc.lng = 11.854860
        }
        hillfort.loc.zoom = 15f
        val loc = LatLng(hillfort.loc.lat, hillfort.loc.lng)

        val options = MarkerOptions()
            .title(hillfort.name)
            .position(loc)
        map.addMarker(options)  //add Marker
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, hillfort.loc.zoom)) // center marker and zoom in
        view?.showHillfort(hillfort)
    }

    fun doHillfordVisited(visited: Boolean){
        if(visited){
            hillfort.dateVisited = Date()
        }
        else{
            hillfort.dateVisited = null
        }
        view?.showDateVisited(hillfort.dateVisited)
    }

    fun loadHillfort(){
        view?.showHillfort(hillfort)
    }

    fun doCacheHillford(name: String, desc: String, notes: String){
        hillfort.name = name
        hillfort.desc = desc
        hillfort.notes = notes
    }

    fun doSelectImage(image: String? = null, index: Int? = null){
        view?.also{
            if(image != null && index != null){
                previousImage = index
            }
            else{
                previousImage = null
            }
            showImagePicker(view!!, IMAGE_REQ_ID)
        }
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when(requestCode){
            IMAGE_REQ_ID -> {
                if(data != null){
                    val newImgPath = data.data.toString()
                    if(previousImage != null){
                        // attempt to change image
                        if(!newImgPath.equals(hillfort.images[previousImage!!])){
                            hillfort.images[previousImage!!] = newImgPath
                        }
                    }
                    else{
                        // add image
                        hillfort.images.add(newImgPath)
                    }
                    view?.info("Images ${hillfort.images.toString()}")
                    view?.showHillfort(hillfort)
                }
            }
        }
    }

    fun doAddOrSave(name: String, desc: String, notes: String, visitedOn: Date?){
        view?.info("Value of visitedOn: $visitedOn")
        hillfort.also{
            it.name = name
            it.desc = desc
            it.notes = notes
            it.dateVisited = visitedOn
        }
        if(editMode){
            app.hillforts.update(hillfort)
        }
        else{
            app.hillforts.create(hillfort)
        }
        view?.finish()
    }

    fun doCancel(){
        view?.finish()
    }

    fun doDelete(){
        app.hillforts.delete(hillfort)
        view?.finish()
    }
}