package de.tp.hillforts.views.login

import android.os.Bundle
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.login_view.*

class LoginView : BaseView() {

  lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_Hillforts)
    super.onCreate(savedInstanceState)
    //setContentView(R.layout.login_view) change to fragment
    setContentView(R.layout.main_fragment)

    if (savedInstanceState == null) {
      val fragment = LoginFragment()
      supportFragmentManager
        .beginTransaction()
        .add(R.id.main_content, fragment)
        .commit()
    }

    /* NOW IN FRAGMENT
    // hide progress at the beginning
    hideProgress()

    // initialize presenter
    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

    // initialize view
    init(toolbar, false)

    bSignIn.setOnClickListener {
      showProgress()
      val email = etEmail.text.toString()
      val pw = etPassword.text.toString()

      if(email == "" || email.isEmpty() || pw == "" || pw.isEmpty()){
        hideProgress()
        toast(getString(R.string.toast_login_invalid_input))
      }
      else{
        presenter.doLogin(email, pw)
      }
    }

    bSignUp.setOnClickListener {
      showProgress()
      val email = etEmail.text.toString()
      val pw = etPassword.text.toString()

      if(email == "" || email.isEmpty() || pw == "" || pw.isEmpty()){
        hideProgress()
        toast("Please enter email and password")
      }
      else{
        presenter.doSignup(email, pw)
      }
    }

     */
  }

  /* NOW IN FRAGMENT
  override fun showProgress(){
    pbLogin.visibility = View.VISIBLE
    tvLogin.visibility = View.VISIBLE
    ivAppLogo.visibility = View.INVISIBLE
    tvLogin.text = getString(R.string.info_logging_in)
  }

  override fun hideProgress(){
    pbLogin.visibility = View.GONE
    tvLogin.visibility = View.GONE
    ivAppLogo.visibility = View.VISIBLE
  }

   */

}