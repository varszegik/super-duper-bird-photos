package hu.bme.aut.superbirdphotographer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import hu.bme.aut.superbirdphotographer.ui.screens.components.BirdPhotographerApp


class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
//                CameraPermissionRequest(
//                    navigateToSettingsScreen = {},
//                    permissionGrantedComponent = { Text("Permission granteeed camera ready") })
//            }
//        }
        setContent {
            BirdPhotographerApp()
        }

    }

    @ExperimentalPermissionsApi
    @Composable
    private fun CameraPermissionRequest(
        navigateToSettingsScreen: () -> Unit,
        permissionGrantedComponent: @Composable () -> Unit
    ) {
        var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

        val cameraPermissionState = rememberPermissionState(
            android.Manifest.permission.CAMERA
        )
        when {
            cameraPermissionState.hasPermission -> {
                permissionGrantedComponent()
            }
            cameraPermissionState.shouldShowRationale ||
                    !cameraPermissionState.permissionRequested -> {
                if (doNotShowRationale) {
                    Text("Feature not available")
                } else {
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("Permission required") },
                        text = { Text("We need your camera pls") },
                        confirmButton = {
                            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                                Text("Request permission")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { doNotShowRationale = true }) {
                                Text("Don't show rationale again")
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

}
