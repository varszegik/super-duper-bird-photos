package hu.bme.aut.superbirdphotographer.ui.screens.home

import android.graphics.Rect
import android.net.Uri
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.superbirdphotographer.data.birddetector.BirdInfoScreen
import hu.bme.aut.superbirdphotographer.data.birddetector.BirdRecognizerImageAnalyzer
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import javax.inject.Inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@HiltViewModel
@Module
@InstallIn(ViewModelComponent::class)
class HomeViewModel @Inject constructor(

) : ViewModel(), BirdInfoScreen {
    private var capturing = false
    val imageAnalyzer: BirdRecognizerImageAnalyzer = BirdRecognizerImageAnalyzer(this)
    @Provides
    fun provideSelf(): BirdInfoScreen {
        return this;
    }
    var captureFileUri by mutableStateOf<Uri?>(null)
        private set
    lateinit var outputFolder: File


    override val imageRect: MutableLiveData<Rect?> by lazy { MutableLiveData<Rect?>() }

    val imageCapture: ImageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()


    override fun takePicture() {
        runBlocking {
            if (!capturing) {
                capturing = true
                CoroutineScope(Dispatchers.IO).launch {
                    delay(5000L)
                    capturing = false
                    captureFileUri = null
                }
                capture()
            }
        }
    }

    fun capture() {
        val photoFile = createFile(outputFolder, FILENAME, PHOTO_EXTENSION)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()

        imageCapture.takePicture(outputOptions, Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    captureFileUri = output.savedUri ?: Uri.fromFile(photoFile)
                    Log.d(TAG, "Photo capture succeeded: $captureFileUri")
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture exception: $exception")
                }
            })
    }

    companion object {
        private const val TAG = "HomeViewModel"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"

        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.GERMAN)
                    .format(System.currentTimeMillis()) + extension
            )

    }

}