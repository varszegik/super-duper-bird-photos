package hu.bme.aut.superbirdphotographer

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import hu.bme.aut.superbirdphotographer.ui.theme.SuperBirdPhotographerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isPermissionGranted: Boolean? = true

        val launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                isPermissionGranted = isGranted
            }
        setContent {
            SuperBirdPhotographerTheme {
                Surface(color = MaterialTheme.colors.background) {

                    when (isPermissionGranted) {
                        true -> Greeting("")
                        false -> Text("permission pls")
                        null -> Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                            Text(text = "Start!")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context).apply {
                setBackgroundColor(Color.GREEN)
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                scaleType = PreviewView.ScaleType.FILL_START
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            val executor = ContextCompat.getMainExecutor(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize(),
    )
}
