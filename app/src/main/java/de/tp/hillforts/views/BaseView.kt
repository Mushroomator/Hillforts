package de.tp.hillforts.views

import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import androidx.appcompat.widget.Toolbar
import de.tp.hillforts.models.HillfortModel

open abstract class BaseView(): AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

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
     * @author Thomas Pilz
     */
    fun init(toolbar: Toolbar){
        toolbar.title = title
        setSupportActionBar(toolbar)
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
}