package de.tp.hillforts.views.camera

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.camera_view.*
import kotlinx.android.synthetic.main.login_view.*
import kotlinx.android.synthetic.main.login_view.view.*
import org.jetbrains.anko.toast

class CameraFragment: Fragment() {

    lateinit var presenter: CameraFragmentPresenter
    lateinit var hostView: View
    lateinit var safeContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hostView = inflater.inflate(R.layout.camera_view, container, false)

        return hostView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init presenter
        presenter = CameraFragmentPresenter(this)

        // Request camera permissions
        if (presenter.allPermissionsGranted()) {
            presenter.startCamera(pvCamera)
        } else {
            presenter.doRequestPermission()
        }

        // Set up the listener for take photo button
        camera_capture_button.setOnClickListener { presenter.takePhoto() }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        presenter.doOnRequestPermissonsResult(pvCamera, requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.doShutdown()
    }

}