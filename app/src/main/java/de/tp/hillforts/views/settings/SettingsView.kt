package de.tp.hillforts.views.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import de.tp.hillforts.views.login.LoginPresenter
import kotlinx.android.synthetic.main.login_view_portrait.*
import kotlinx.android.synthetic.main.login_view_portrait.toolbar
import kotlinx.android.synthetic.main.settings_view_portrait.*
import kotlin.math.ceil

class SettingsView : BaseView() {

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_view_portrait)

        // initialize presenter
        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        // initialize view
        init(toolbar, true)
        presenter.doLoadUser()
        presenter.doLoadStatistics()
    }

    /**
     * Show a user's information on screen.
     * @param email email address
     * @param password password (will be shown in plain text!!! as per requirements)
     * @author Thomas Pilz
     */
    fun showUserInfo(email: String, password: String){
        tvUserEmailVal.text = email
        tvUserPasswordVal.text = password
    }

    /**
     * Show a user's statistics on screen.
     * @param total total number of hillforts
     * @param visited number of visited hillforts
     * @author Thomas Pilz
     */
    fun showStatistics(total: Int, visited: Int){
        tvTotalSitesVal.text = total.toString()
        tvVisitedSitesVal.text = visited.toString()
        var progress = 0
        if (total != 0){
            val pct = (visited.toDouble() / total.toDouble()) * 100
            progress = pct.toInt() // will autom. be ceiled
        }
        pbVisitdProgress.setProgress(progress, true)
        tvPctVisitedVal.text = "$progress%"
    }
}