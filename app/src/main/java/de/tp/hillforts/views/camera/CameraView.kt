package de.tp.hillforts.views.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.camera_view.*
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.concurrent.Executors

class CameraView : BaseView() {

    lateinit var presenter: CameraPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_view)
        // no toolbar/ up-support

        // Request camera permissions
        if (presenter.allPermissionsGranted()) {
            presenter.startCamera(pvCamera)
        } else {
            presenter.doRequestPermission()
        }

        // init presenter
         presenter = initPresenter(CameraPresenter(this)) as CameraPresenter

        // Set up the listener for take photo button
        camera_capture_button.setOnClickListener { presenter.takePhoto() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        presenter.doOnRequestPermissonsResult(pvCamera, requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.doShutdown()
    }

}