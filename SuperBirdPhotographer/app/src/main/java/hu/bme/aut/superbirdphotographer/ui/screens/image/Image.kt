package hu.bme.aut.superbirdphotographer.ui.screens.image

import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext

@Composable
fun ImageScreen(uri: String) {
    val context = LocalContext.current
    val image = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(uri))
    Image(BitmapPainter(image.asImageBitmap()), "image", modifier = Modifier.fillMaxSize().rotate(90F))
}