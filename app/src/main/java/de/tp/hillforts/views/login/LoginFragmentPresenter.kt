package de.tp.hillforts.views.login

import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
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
        if (auth.currentUser != null){
            // when navigation back to this activity log the user out and clear hillforts!
            auth.signOut()
            app.hillforts.clear()
        }
    }

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(hostActivity!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null){
                    fireStore!!.fetchHillforts {
                        view?.hideProgress()
                        view?.findNavController()?.navigate(R.id.loginToHillfortList)
                    }
                }
                else{
                    view?.hideProgress()
                    view?.findNavController()?.navigate(R.id.loginToHillfortList)
                }
            } else {
                view?.hideProgress()
                Toast.makeText(view?.context, "${view?.getString(R.string.toast_fb_signin_failed)}: ${task.exception?.message}", Toast.LENGTH_LONG).show()
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
                        view?.findNavController()?.navigate(R.id.loginToHillfortList)
                    }
                }
                else{
                    view?.hideProgress()
                    view?.findNavController()?.navigate(R.id.loginToHillfortList)
                }
            } else {
                view?.hideProgress()
                Toast.makeText(view?.context, "${view?.getString(R.string.toast_fb_signup_failed)}: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}