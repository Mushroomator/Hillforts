package de.tp.hillforts.views.login

import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.info

class LoginPresenter(view: BaseView): BasePresenter(view) {

    /**
     * Login user after checking credentials.
     * @param email email address of user
     * @param password password of user
     * @author Thomas Pilz
     */
    fun doLogin(email: String, password: String){
        view?.info("User $email signed in.")
        view?.navigateTo(VIEW.LIST)
    }

    /**
     * Sing up a user in the repo.
     * @param email email address of user
     * @param password password of user
     * @author Thomas Pilz
     */
    fun doSignup(email: String, password: String){
        view?.info("User $email signed up.")
        view?.navigateTo(VIEW.LIST)
    }
}