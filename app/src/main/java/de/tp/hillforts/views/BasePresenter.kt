package de.tp.hillforts.views

import android.content.Intent
import de.tp.hillforts.main.MainApp

open abstract class BasePresenter(var view: BaseView?) {

    var app: MainApp = view?.application as MainApp

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    }

    open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    }

    open fun onDestroy(){
        view = null
    }
}