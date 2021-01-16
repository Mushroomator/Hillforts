package de.tp.hillforts.views.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import de.tp.hillforts.R
import de.tp.hillforts.views.login.LoginView

class SplashScreenActitivity : AppCompatActivity() {

    private val handler: Handler = Handler()

    private val runnable = Runnable{
        if(!isFinishing){
            startActivity(Intent(applicationContext, LoginView::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // actually bad practice as load takes longer but load time is so quick that splash screen would never be displayed
        // show splash screen for one second
        handler.postDelayed(runnable, 1000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }


}