package hu.bme.aut.superbirdphotographer.ui.screens.images

import android.os.Build
import android.util.Size
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@ExperimentalFoundationApi
@Composable
fun Images(viewModel: ImagesViewModel, openDrawer: () -> Unit) {
    val context = LocalContext.current
    val images = viewModel.listImages(context.contentResolver)
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
        LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 128.dp)) {
            items(images) { image ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val thumbnail = context.contentResolver.loadThumbnail(image.contentUri, Size(320, 320), null)

                    Image(BitmapPainter(thumbnail.asImageBitmap()), "image", modifier = Modifier.width(128.dp).height(128.dp).padding(5.dp), contentScale = ContentScale.Crop)
                }
            }
        }
    }
}