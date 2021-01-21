package de.tp.hillforts.views.login

import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.helpers.AuthProvider
import de.tp.hillforts.views.BasePresenter
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class LoginPresenter(view: LoginView): BasePresenter(view) {

    //-------------
    // No longer necessary with Firebase
    //--------------
    /*
    var auth: AuthProvider = AuthProvider

    /**
     * Login user after checking credentials.
     * @param email email address of user
     * @param password password of user
     * @author Thomas Pilz
     */
    fun doLogin(email: String, password: String){
        val success = auth.signIn(email, password)
        if (success){
            view?.info(view?.getString(R.string.log_signin_successful, email))
            app.initHillfortRepo(AuthProvider.currentUser!!)
            view?.navigateTo(VIEW.LIST)
        }
        else{
            view?.toast(view?.getString(R.string.toast_signin_failed)!!)
        }
    }

    /**
     * Sing up a user in the repo.
     * @param email email address of user
     * @param password password of user
     * @author Thomas Pilz
     */
    fun doSignup(email: String, password: String){
        val success = auth.signUp(email, password)
        if (success){
            view?.info(view?.getString(R.string.log_signup_successful, email))
            app.initHillfortRepo(AuthProvider.currentUser!!)
            view?.navigateTo(VIEW.LIST)
        }
        else{
            view?.toast(view?.getString(R.string.toast_signup_failed)!!)
        }
    }
    */

    var auth = FirebaseAuth.getInstance()

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                view?.navigateTo(VIEW.LIST)
            } else {
                view?.toast("${view?.getString(R.string.toast_fb_signup_failed)}: ${task.exception?.message}")
            }
            view?.hideProgress()
        }
    }

    fun doSignup(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                view?.navigateTo(VIEW.LIST)
            } else {
                view?.toast("${view?.getString(R.string.toast_fb_signup_failed)}: ${task.exception?.message}")
            }
            view?.hideProgress()
        }
    }
}