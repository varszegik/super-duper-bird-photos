package hu.bme.aut.superbirdphotographer.data.birddetector

import android.content.Context
import android.graphics.*
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.nio.ByteBuffer
import javax.inject.Inject

import android.media.Image
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.renderscript.Type
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.toRect
import java.io.ByteArrayOutputStream

@Module
@InstallIn(ViewModelComponent::class)
class BirdRecognizerImageAnalyzer @Inject constructor(
    private val birdInfoScreen: BirdInfoScreen,
    private val context: Context
) : ImageAnalysis.Analyzer {
//    private val localModel =
//        LocalModel.Builder().setAssetFilePath("model.tflite")
//            .build()
//    private val customObjectDetectorOptions = CustomObjectDetectorOptions.Builder(localModel)
//        .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE).enableClassification()
//        .setClassificationConfidenceThreshold(0.8f)
//        .setMaxPerObjectLabelCount(1)
//        .build()

    var options: ObjectDetector.ObjectDetectorOptions =
        ObjectDetector.ObjectDetectorOptions.builder()
            .setBaseOptions(BaseOptions.builder().useGpu().build())
            .setMaxResults(1)
            .build()
    var objectDetector = ObjectDetector.createFromFileAndOptions(context, "model.tflite", options)

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {

            val image2 = TensorImage.fromBitmap(mediaImage.toBitmap())
//            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val detection = objectDetector.detect(image2)

//                .addOnSuccessListener { results ->
                    birdInfoScreen.imageRect.value = null
                    for (detectedObject in detection) {
                        val boundingBox = detectedObject.boundingBox
//                        val label = detectedObject.labels.firstOrNull()
//                        if(label != null && label.text != "None"){
                            birdInfoScreen.imageRect.value = boundingBox.toRect()
//                            birdInfoScreen.takePicture(label.text)
//                            val text = label.text
                            Log.d("results", detectedObject.categories[0].label)
//                        }

                    }
//                }
//                .addOnFailureListener { e ->
//                    Log.e("results", e.toString())
//                }
//                .addOnCompleteListener {
            mediaImage.close()
            imageProxy.close()
//                }
        }
    }
}

fun Image.toBitmap(): Bitmap {
    val yBuffer = planes[0].buffer // Y
    val vuBuffer = planes[2].buffer // VU

    val ySize = yBuffer.remaining()
    val vuSize = vuBuffer.remaining()

    val nv21 = ByteArray(ySize + vuSize)

    yBuffer.get(nv21, 0, ySize)
    vuBuffer.get(nv21, ySize, vuSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}