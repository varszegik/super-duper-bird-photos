package hu.bme.aut.superbirdphotographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToImages: () -> Unit,
    navigateToSettings: () -> Unit,
    closeDrawer: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(24.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
        MenuItem(
            label = "Live view",
            action = { navigateToHome(); closeDrawer() },
            isSelected = currentRoute == MainDestinations.HOME_ROUTE,
            icon = Icons.Filled.Home
        )
        MenuItem(
            label = "Images",
            action = { navigateToImages(); closeDrawer() },
            isSelected = currentRoute == MainDestinations.IMAGES_ROUTE,
            icon = Icons.Outlined.Send
        )
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
        MenuItem(
            label = "Settings",
            action = { navigateToSettings(); closeDrawer() },
            isSelected = currentRoute == MainDestinations.SETTINGS_ROUTE,
            icon = Icons.Filled.Settings
        )

    }
}

@Composable
fun MenuItem(
    label: String,
    isSelected: Boolean = false,
    action: () -> Unit = {},
    icon: ImageVector
) {

    val colors = MaterialTheme.colors
    val imageAlpha = if (isSelected) {
        1f
    } else {
        0.6f
    }
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = backgroundColor,
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        TextButton(onClick = action, modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(icon, "menu", tint = textIconColor)
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}

@Preview
@Composable
fun MenuItemPreview() {
    Column {

        MenuItem("Home", true, icon = Icons.Filled.Home)
        MenuItem("Home", false, icon = Icons.Filled.Home)
    }

}