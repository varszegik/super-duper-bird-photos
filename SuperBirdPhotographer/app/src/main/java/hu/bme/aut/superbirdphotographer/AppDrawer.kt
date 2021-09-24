package hu.bme.aut.superbirdphotographer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    navigateToHome: () -> Unit,
    navigateToImages: () -> Unit,
    closeDrawer: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(24.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
        Button(onClick = { navigateToHome(); closeDrawer() }) {
            Text(text = "Live view")
        }
        Button(onClick = { navigateToImages(); closeDrawer() }) {
            Text(text = "Images")
        }
        Button(onClick = { navigateToImages(); closeDrawer() }) {
            Text(text = "Settings")
        }
    }
}