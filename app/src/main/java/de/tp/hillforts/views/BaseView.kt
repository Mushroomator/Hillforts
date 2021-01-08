package de.tp.hillforts.views

import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import androidx.appcompat.widget.Toolbar

open abstract class BaseView(): AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun initPresenter(presenter: BasePresenter): BasePresenter{
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar){
        toolbar.title = title
        setSupportActionBar(toolbar)
    }
}