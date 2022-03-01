package hu.bme.aut.superbirdphotographer.ui.screens.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Environment
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.core.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import hu.bme.aut.superbirdphotographer.R
import hu.bme.aut.superbirdphotographer.data.birddetector.BirdRecognizerImageAnalyzer
import hu.bme.aut.superbirdphotographer.data.cloud.GoogleDriveRepository
import android.util.Size as DetectionSize


@ExperimentalPermissionsApi
@Composable
fun Home(
    viewModel: HomeViewModel,
    openDrawer: () -> Unit
) {
    val context = LocalContext.current
    viewModel.outputFolder =
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
    viewModel.contentResolver = context.contentResolver
    viewModel.cloudImagesRepository = GoogleDriveRepository(context)
    viewModel.sharedPreferences = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
    viewModel.imageAnalyzer = BirdRecognizerImageAnalyzer(viewModel, context)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Live view")
                },
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(Icons.Filled.Menu, "menu")
                    }
                },
            )
        },

        )
    {
        WithCameraPermission {
            CameraView(viewModel.imageAnalyzer, viewModel.imageCapture)
            val objectRect: Rect? by viewModel.imageRect.observeAsState()
            ObjectRectangle(objectRect)
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                IconButton(onClick = { viewModel.takePicture("Manual capture") }) {
                    Icon(painterResource(R.drawable.ic_shutter), "capture")
                }
            }
        }
    }
}

@Composable
fun ObjectRectangle(rect: Rect?) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (rect == null) return@Canvas
        Log.d("rect", "${rect.top}, ${rect.left}, ${rect.bottom}, ${rect.right}")
        val topOffset = (rect.top / 1080f) * size.height
        val leftOffset = (rect.left / 1080f) * size.width
        val right = (rect.right / 1080f) * size.width
        val bottom = (rect.bottom / 1080f) * size.height
            translate(left = leftOffset, top = topOffset){
                drawRect(
                    color = Color.Green,
                    style = Stroke(width = 8f),
                    size = Size(
                        kotlin.math.abs(right - leftOffset),
                        kotlin.math.abs(bottom - topOffset)
                    ),
                )
            }

    }
}


@Composable
fun CameraView(
    analyser: ImageAnalysis.Analyzer,
    imageCapture: ImageCapture
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val color = MaterialTheme.colors.surface.toArgb()
    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context).apply {
                setBackgroundColor(color)
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                scaleType = PreviewView.ScaleType.FILL_START
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(DetectionSize(480, 360))
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyser)



                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis,
                    imageCapture
                )
            }, ContextCompat.getMainExecutor(context))
            previewView
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@ExperimentalPermissionsApi
@Composable
fun WithCameraPermission(permissionGrantedComponent: @Composable () -> Unit) {
    val activity = (LocalContext.current as? Activity)
    CameraPermissionRequest(
        onPermissionPermanentlyDenied = {
            activity?.finish()
        },
        navigateToSettingsScreen = {},
        permissionGrantedComponent = permissionGrantedComponent
    )
}

@ExperimentalPermissionsApi
@Composable
fun CameraPermissionRequest(
    navigateToSettingsScreen: () -> Unit,
    onPermissionPermanentlyDenied: () -> Unit,
    permissionGrantedComponent: @Composable () -> Unit
) {
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )
    when {
        cameraPermissionState.hasPermission -> {
            permissionGrantedComponent()
        }
        cameraPermissionState.shouldShowRationale ||
                !cameraPermissionState.permissionRequested -> {
            if (doNotShowRationale) {
                onPermissionPermanentlyDenied()
            } else {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Permission required") },
                    text = { Text("In order to take photos of the beautiful birds in sight, we need to access your device's camera.") },
                    confirmButton = {
                        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { doNotShowRationale = true }) {
                            Text("No, don't ask again")
                        }
                    }
                )
            }
        }
        else -> {
            Column {
                Text(
                    "Camera permission denied. See this FAQ with information about why we " +
                            "need this permission. Please, grant us access on the Settings screen."
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = navigateToSettingsScreen) {
                    Text("Open Settings")
                }
            }
        }
    }
}