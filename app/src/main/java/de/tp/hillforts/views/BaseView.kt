package de.tp.hillforts.views

import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import androidx.appcompat.widget.Toolbar
import de.tp.hillforts.models.hillfort.HillfortModel
import de.tp.hillforts.views.editLocation.EditLocationView
import de.tp.hillforts.views.hillfordList.HillfordListView
import de.tp.hillforts.views.hillfortDetails.HillfortDetailsView
import de.tp.hillforts.views.login.LoginView
import de.tp.hillforts.views.hillfortMap.HillfortMapView
import de.tp.hillforts.views.settings.SettingsView
import org.jetbrains.anko.info
import java.util.*

abstract class BaseView(): AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    protected val dateFormat = "dd/MM/yyyy"

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent()
        when (view) {
            VIEW.LIST -> intent = Intent(this, HillfordListView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
            VIEW.DETAILS -> intent = Intent(this, HillfortDetailsView::class.java)
            VIEW.EDIT_LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.MAP -> intent = Intent(this, HillfortMapView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    /**
     * Initialize the Presenter in BasePresenter.
     * @param presenter presenter
     * @author Thomas Pilz
     */
    fun initPresenter(presenter: BasePresenter): BasePresenter{
        basePresenter = presenter
        return presenter
    }

    /**
     * Initialize common view components e.g. toolbar.
     * @param toolbar toolbar to be initialized
     * @param upEnabled enable "Up/Back" button on activity
     * @author Thomas Pilz
     */
    fun init(toolbar: Toolbar, upEnabled: Boolean = true){
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        info("activity result $basePresenter")
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }


    /**
     * Show all hillforts on the screen.
     * @param hillforts hillforts to be displayed
     * @author Thomas Pilz
     */
    open fun showHillforts(hillforts: List<HillfortModel>){}

    /**
     * Show hillfort on the screen.
     * @param hillfort hillford to be displayed
     * @author Thomas Pilz
     */
    open fun showHillfort(hillfort: HillfortModel){}

    open fun showDateVisited(date: Date?){}

    open fun updateLocation(lat: Double, lng: Double){}
}