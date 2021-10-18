package hu.bme.aut.superbirdphotographer.ui.screens.images

import android.content.Context
import android.os.Build
import android.util.Size
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import hu.bme.aut.superbirdphotographer.data.local.MediaStoreImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalFoundationApi
@Composable
fun Images(
    viewModel: ImagesViewModel,
    openDrawer: () -> Unit,
    navigateToImageScreen: (imageUri: String?) -> Unit
) {
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
        LazyColumn(modifier = Modifier.fillMaxSize(), content = {
            images.forEach { (date, list) ->
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp)
                            .background(MaterialTheme.colors.surface),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.GERMAN).format(date))
                    }
                }
                items(list) { image ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val thumbnail = context.contentResolver.loadThumbnail(
                            image.contentUri,
                            Size(320, 320),
                            null
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp)
                                .clickable { navigateToImageScreen(URLEncoder.encode(image.contentUri.toString(), StandardCharsets.UTF_8.toString())) },
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            Image(
                                BitmapPainter(thumbnail.asImageBitmap()),
                                "image",
                                modifier = Modifier
                                    .width(128.dp)
                                    .height(128.dp)
                                    .padding(5.dp, 0.dp, 30.dp, 0.dp),
                                contentScale = ContentScale.Crop
                            )
                            image.species?.let { it1 -> Text(it1) }
                        }

                    }
                }
            }


        })

    }
}

@ExperimentalFoundationApi
@Composable
fun DetectionGroup(context: Context, date: Date?, images: List<MediaStoreImage>) {
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 128.dp)) {
        items(images) { image ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val thumbnail =
                    context.contentResolver.loadThumbnail(image.contentUri, Size(320, 320), null)

                Image(
                    BitmapPainter(thumbnail.asImageBitmap()), "image", modifier = Modifier
                        .width(128.dp)
                        .height(128.dp)
                        .padding(5.dp), contentScale = ContentScale.Crop
                )
            }
        }
    }
}