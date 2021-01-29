package de.tp.hillforts.views.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.login_view.*
import kotlinx.android.synthetic.main.login_view.view.*
import org.jetbrains.anko.toast

class LoginFragment: Fragment() {

    lateinit var presenter: LoginFragmentPresenter
    lateinit var hostView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hostView = inflater.inflate(R.layout.login_view, container, false)

        return hostView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init presenter
        presenter = LoginFragmentPresenter(this)

        hideProgress()

        // sign in button
        bSignIn.setOnClickListener {
            showProgress()
            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()

            if(email == "" || email.isEmpty() || pw == "" || pw.isEmpty()){
                hideProgress()
                Toast.makeText(this.context, getString(R.string.toast_login_invalid_input), Toast.LENGTH_LONG).show()
                //view.toast(getString(R.string.toast_login_invalid_input))
            }
            else{
                presenter.doLogin(email, pw)
            }
        }

        // sign up button
        bSignUp.setOnClickListener {
            showProgress()
            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()

            if(email == "" || email.isEmpty() || pw == "" || pw.isEmpty()){
                hideProgress()
                Toast.makeText(this.context,"Please enter email and password", Toast.LENGTH_LONG).show()
            }
            else{
                presenter.doSignup(email, pw)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun showProgress(){
        pbLogin.visibility = View.VISIBLE
        tvLogin.visibility = View.VISIBLE
        ivAppLogo.visibility = View.INVISIBLE
        tvLogin.text = getString(R.string.info_logging_in)
    }

    fun hideProgress(){
        pbLogin.visibility = View.GONE
        tvLogin.visibility = View.GONE
        ivAppLogo.visibility = View.VISIBLE
    }
}