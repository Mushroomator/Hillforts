package de.tp.hillforts.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.login_view_portrait.*

class LoginView : BaseView() {

  lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_view_portrait)

    // initialize presenter
    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

    // initialize view
    init(toolbar)

    bSignIn.setOnClickListener {    }
    bSignUp.setOnClickListener {  }
  }
}