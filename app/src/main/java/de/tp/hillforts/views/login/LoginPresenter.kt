package de.tp.hillforts.views.login

import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.info

class LoginPresenter(view: BaseView): BasePresenter(view) {

    fun doLogin(email: String, password: String){
        view?.info("User $email signed in.")
        view?.navigateTo(VIEW.LIST)
    }

    fun doSignup(email: String, password: String){
        view?.info("User $email signed up.")
        view?.navigateTo(VIEW.LIST)
    }
}