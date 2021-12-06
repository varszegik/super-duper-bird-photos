package hu.bme.aut.superbirdphotographer.data.birddetector

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
class BirdRecognizerImageAnalyzer @Inject constructor(private val birdInfoScreen: BirdInfoScreen) :  ImageAnalysis.Analyzer {
    private val localModel =
        LocalModel.Builder().setAssetFilePath("lite-model_aiy_vision_classifier_birds_V1_3.tflite")
            .build()
    private val customObjectDetectorOptions = CustomObjectDetectorOptions.Builder(localModel)
        .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE).enableClassification()
        .setClassificationConfidenceThreshold(0.8f)
        .setMaxPerObjectLabelCount(1)
        .build()
    private val objectDetector = ObjectDetection.getClient(customObjectDetectorOptions)




    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            objectDetector.process(image)
                .addOnSuccessListener { results ->
                    birdInfoScreen.imageRect.value = null
                    for (detectedObject in results) {
                        val boundingBox = detectedObject.boundingBox
                        val label = detectedObject.labels.firstOrNull()
                        if(label != null && label.text != "None"){
                            birdInfoScreen.imageRect.value = boundingBox
                            birdInfoScreen.takePicture(label.text)
                            val text = label.text
                            Log.d("results", text)
                        }

                    }
                }
                .addOnFailureListener { e ->
                    Log.e("results", e.toString())
                }
                .addOnCompleteListener {
                    mediaImage.close()
                    imageProxy.close()
                }
        }
    }
}