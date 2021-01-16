package de.tp.hillforts.views.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.login.LoginPresenter
import kotlinx.android.synthetic.main.login_view_portrait.*

class SettingsView : BaseView() {

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_view_portrait)

        // initialize presenter
        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        // initialize view
        init(toolbar, true)
    }
}