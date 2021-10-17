package hu.bme.aut.superbirdphotographer.ui.screens.home

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.superbirdphotographer.data.birddetector.BirdInfoScreen
import hu.bme.aut.superbirdphotographer.data.birddetector.BirdRecognizerImageAnalyzer
import hu.bme.aut.superbirdphotographer.data.cloud.CloudImagesRepository
import hu.bme.aut.superbirdphotographer.data.cloud.GoogleDriveRepository
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
    lateinit var cloudImagesRepository: CloudImagesRepository


    @Provides
    fun provideSelf(): BirdInfoScreen {
        return this;
    }

    var captureFilePath by mutableStateOf<String?>(null)
        private set
    lateinit var outputFolder: File
    lateinit var contentResolver: ContentResolver


    override val imageRect: MutableLiveData<Rect?> by lazy { MutableLiveData<Rect?>() }

    val imageCapture: ImageCapture =
        ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()


    override fun takePicture() {
        runBlocking {
            if (!capturing) {
                capturing = true
                CoroutineScope(Dispatchers.IO).launch {
                    delay(5000L)
                    capturing = false
                    captureFilePath = null
                }
                capture()
            }
        }
    }

    fun capture() {
        val fileName = generateFileName(FILENAME, PHOTO_EXTENSION)
        captureFilePath =
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.path + File.separator + IMAGES_SUBDIRECTORY + File.separator + fileName
        val newImageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + '/' + IMAGES_SUBDIRECTORY
                )
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            newImageDetails
        ).build()


        imageCapture.takePicture(outputOptions, Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri?.let {
                        val filePath = getRealPathFromURI(it)
                        val file = File(filePath)
                        cloudImagesRepository.uploadImage(file, IMAGES_SUBDIRECTORY, "image/jpeg")
                    }
                    Log.d(TAG, "Photo capture succeeded")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture exception: $exception")
                }
            })
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    companion object {
        private const val TAG = "HomeViewModel"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val IMAGES_SUBDIRECTORY = "BirdPhotography"


        private fun generateFileName(format: String, extension: String): String {
            return SimpleDateFormat(format, Locale.GERMAN)
                .format(System.currentTimeMillis()) + extension
        }

    }

}