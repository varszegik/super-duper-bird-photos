package hu.bme.aut.superbirdphotographer.images

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

@Composable
fun Images(openDrawer: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Images")
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
        Text("images")
    }
}