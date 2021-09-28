package hu.bme.aut.superbirdphotographer.ui.screens.home

import android.Manifest
import android.app.Activity
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.core.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.graphics.toArgb

@ExperimentalPermissionsApi
@Composable
fun Home(
    viewModel: HomeViewModel = viewModel(),
    openDrawer: () -> Unit
) {
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
            CameraView()
        }
    }
}

@Composable
fun CameraView() {
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

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
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