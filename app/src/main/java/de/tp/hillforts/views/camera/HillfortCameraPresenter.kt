package de.tp.hillforts.views.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.tp.hillforts.R
import de.tp.hillforts.views.BasePresenter
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HillfortCameraPresenter(view: HillfortCameraView): BasePresenter(view) {

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
        val photoFile = File(outputDirectory,SimpleDateFormat(FILENAME_FORMAT, Locale.GERMANY).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(view), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    view?.info("${view?.getString(R.string.toast_photo_failed)}: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // image has been save successfully
                    val msg = view?.getString(R.string.toast_photo_saved)
                    view?.toast(msg.toString())
                    view?.info(msg)

                    // send image path back to caller activity
                    val savedUri = Uri.fromFile(photoFile)
                    val resultIntent = Intent()
                    resultIntent.putExtra(TAKE_PHOTO, savedUri.toString())
                    view?.setResult(Activity.RESULT_OK, resultIntent)
                    view?.finish()
                }
            })
    }

    fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(view!!)

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
                cameraProvider.bindToLifecycle(view!!, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(view))
        imageCapture = ImageCapture.Builder()
            .build()
    }

    fun doRequestPermission(){
        ActivityCompat.requestPermissions(view!!,
            HillfortCameraPresenter.REQUIRED_PERMISSIONS,
            HillfortCameraPresenter.REQUEST_CODE_PERMISSIONS
        )
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
                view?.info(view?.getString(R.string.photo_permission_denied))
                view?.finish()
            }
        }
    }

    fun getOutputDirectory(): File {
        val mediaDir = view?.externalMediaDirs!!.firstOrNull()?.let {
            File(it, view?.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else view!!.filesDir
    }

    fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            view!!.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Shutdown the camera executer.
     */
    fun doShutdown(){
        cameraExecutor.shutdown()
    }
}