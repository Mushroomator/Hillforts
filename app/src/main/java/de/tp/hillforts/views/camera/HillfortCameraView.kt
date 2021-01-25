package de.tp.hillforts.views.camera

import android.os.Bundle
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.camera_view.*

class HillfortCameraView : BaseView() {
/*
    lateinit var presenter: HillfortCameraPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_view)
        // no toolbar/ up-support

        // init presenter
        presenter = initPresenter(HillfortCameraPresenter(this)) as HillfortCameraPresenter

        // Request camera permissions
        if (presenter.allPermissionsGranted()) {
            presenter.startCamera(pvCamera)
        } else {
            presenter.doRequestPermission()
        }

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

 */

}