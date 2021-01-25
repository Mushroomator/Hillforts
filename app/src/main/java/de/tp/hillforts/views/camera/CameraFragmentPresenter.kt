package de.tp.hillforts.views.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.Tag
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import de.tp.hillforts.R
import de.tp.hillforts.models.hillfort.HillfortFireStore
import de.tp.hillforts.views.BasePresenterFragment
import de.tp.hillforts.views.VIEW
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragmentPresenter(var view: CameraFragment): BasePresenterFragment(view) {
    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private val TAKE_PHOTO = "taken_photo"

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    init {
        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.GERMANY).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(view.context), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.i(TAG,"${view.getString(R.string.toast_photo_failed)}: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // image has been save successfully
                    val msg = view.getString(R.string.toast_photo_saved)
                    Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
                    Log.i(TAG, msg)

                    // send image path back to caller activity
                    val savedUri = Uri.fromFile(photoFile)
                    val action = CameraFragmentDirections.cameraToDetails(hillfort = null, photo = savedUri.toString(), location = null)
                    view.findNavController()?.navigate(action)
                }
            })
    }

    fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(view.requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.createSurfaceProvider())
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(view, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(view.context))
        imageCapture = ImageCapture.Builder()
            .build()
    }

    fun doRequestPermission(){
        ActivityCompat.requestPermissions(this.hostActivity!!, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    /**
     * Check if permission to access the camera was granted by user.
     * If not end the activity.
     */
    fun doOnRequestPermissonsResult(previewView: PreviewView, requestCode: Int, permissions: Array<String>, grantResults: IntArray){
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera(previewView)
            } else {
                Log.i(TAG, view.getString(R.string.photo_permission_denied))
                CameraFragmentDirections.cameraToDetails(hillfort = null, photo = null, location = null)
            }
        }
    }

    fun getOutputDirectory(): File {
        val test = view.activity   // workaroud otherwise Android studio seems to complain
        val mediaDir = test?.externalMediaDirs!!.firstOrNull()?.let {
            File(it, view.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else test.filesDir
    }

    fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        val ctx = view.activity?.baseContext
        ContextCompat.checkSelfPermission(
            ctx!!, it) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Shutdown the camera executer.
     */
    fun doShutdown(){
        cameraExecutor.shutdown()
    }
}