package de.tp.hillforts.views.login

import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import org.jetbrains.anko.info

class LoginPresenter(view: BaseView): BasePresenter(view) {

    fun doLogin(email: String, password: String){
        view?.info("User $email signed in.")
    }

    fun doSignup(email: String, password: String){
        view?.info("User $email signed up.")
    }
}