package de.tp.hillforts.views.login

import android.os.Bundle
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.login_view.*
import org.jetbrains.anko.toast

class LoginView : BaseView() {

  lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_Hillforts)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_view)

    // initialize presenter
    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

    // initialize view
    init(toolbar, false)

    bSignIn.setOnClickListener {
      val email = etEmail.text.toString()
      val pw = etPassword.text.toString()

      if(email == "" || email.isEmpty() || pw == "" || pw.isEmpty()){
        toast(getString(R.string.toast_login_invalid_input))
      }
      else{
        presenter.doLogin(email, pw)
      }
    }

    bSignUp.setOnClickListener {
      val email = etEmail.text.toString()
      val pw = etPassword.text.toString()

      if(email == "" || email.isEmpty() || pw == "" || pw.isEmpty()){
        toast("Please enter email and password")
      }
      else{
        presenter.doSignup(email, pw)
      }
    }
  }

}