package de.tp.hillforts.views.login

import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortFireStore
import de.tp.hillforts.views.BasePresenterFragment
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.toast

class LoginFragmentPresenter(var view: LoginFragment?): BasePresenterFragment(view) {

    var auth = FirebaseAuth.getInstance()
    var fireStore: HillfortFireStore? = null

    init {
        if(app.hillforts is HillfortFireStore){
            fireStore = app.hillforts as HillfortFireStore
        }
    }

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(hostActivity!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null){
                    fireStore!!.fetchHillforts {
                        view?.hideProgress()
                        hostActivity?.navigateTo(VIEW.LIST)
                    }
                }
                else{
                    view?.hideProgress()
                    hostActivity?.navigateTo(VIEW.LIST)
                }
            } else {
                view?.hideProgress()
                hostActivity?.toast("${view?.getString(R.string.toast_fb_signin_failed)}: ${task.exception?.message}")
            }
        }
    }

    fun doSignup(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(hostActivity!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null){
                    fireStore!!.fetchHillforts {
                        view?.hideProgress()
                        hostActivity?.navigateTo(VIEW.LIST)
                    }
                }
                else{
                    view?.hideProgress()
                    hostActivity?.navigateTo(VIEW.LIST)
                }
            } else {
                view?.hideProgress()
                hostActivity?.toast("${view?.getString(R.string.toast_fb_signup_failed)}: ${task.exception?.message}")
            }
        }
    }
}