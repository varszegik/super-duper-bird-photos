package hu.bme.aut.superbirdphotographer.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import com.alorma.settings.composables.SettingsCheckbox
import com.alorma.settings.composables.SettingsGroup
import com.alorma.settings.composables.SettingsSlider
import com.alorma.settings.storage.preferences.rememberPreferenceBooleanSettingState
import com.alorma.settings.storage.preferences.rememberPreferenceFloatSettingState

@Composable
fun Settings(openDrawer: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(Icons.Filled.Menu, "menu")
                    }
                },
            )
        },
    )
    {
        val shouldUploadToDriveState = rememberPreferenceBooleanSettingState(
            key = "should_upload_to_drive",
            defaultValue = true,
        )
        val delayBetweenImagesState = rememberPreferenceFloatSettingState(
            key = "delay_between_images",
            defaultValue = 5f,
        )
        val shouldGroupImages = rememberPreferenceBooleanSettingState(
            key = "should_group_images",
            defaultValue = true,
        )
        val groupImagesIn = rememberPreferenceFloatSettingState(
            key = "group_images_in",
            defaultValue = 5f,
        )
        Column {
            SettingsCheckbox(
                title = { Text(text = "Upload to Google Drive") },
                subtitle = { Text(text = "Newly taken pictures will automatically appear in your Google Drive") },
                state = shouldUploadToDriveState
            )
            SettingsSlider(
                title = { Text(text = "Delay between images: ${delayBetweenImagesState.value.toInt()} seconds") },
                state = delayBetweenImagesState,
                valueRange = 1f..60f,
                steps = 60
            )
            SettingsGroup(title = { Text("Image grouping") }) {
                SettingsCheckbox(
                    title = { Text(text = "Group Images") },
                    subtitle = { Text(text = "Images taken between the given interval will be grouped together") },
                    state = shouldGroupImages
                )
            }
            if (shouldGroupImages.value) {
                SettingsSlider(
                    title = { Text(text = "Group images taken in: ${groupImagesIn.value.toInt()} minutes") },
                    state = groupImagesIn,
                    valueRange = 1f..60f,
                    steps = 60
                )
            }
        }
    }
}

