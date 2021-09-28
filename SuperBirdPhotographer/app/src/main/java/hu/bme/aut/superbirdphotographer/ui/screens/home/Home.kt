package hu.bme.aut.superbirdphotographer.ui.screens.home

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

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
            Text("Hi")
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun WithCameraPermission(permissionGrantedComponent: @Composable () -> Unit) {
    CameraPermissionRequest(
        navigateToSettingsScreen = {},
        permissionGrantedComponent = permissionGrantedComponent
    )
}

@ExperimentalPermissionsApi
@Composable
fun CameraPermissionRequest(
    navigateToSettingsScreen: () -> Unit,
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