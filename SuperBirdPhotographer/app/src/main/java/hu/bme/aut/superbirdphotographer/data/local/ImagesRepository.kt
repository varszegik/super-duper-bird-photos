package hu.bme.aut.superbirdphotographer.data.local

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.query
import android.provider.MediaStore.Images.Thumbnails.query
import android.provider.MediaStore.Video.query
import androidx.recyclerview.widget.DiffUtil
import java.util.*
import java.util.concurrent.TimeUnit

data class MediaStoreImage(
    val id: Long,
    val displayName: String,
    val contentUri: Uri,
    val species: String?,
    val date: String?
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<MediaStoreImage>() {
            override fun areItemsTheSame(oldItem: MediaStoreImage, newItem: MediaStoreImage) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MediaStoreImage, newItem: MediaStoreImage) =
                oldItem == newItem
        }
    }
}


class ImagesRepository {

    fun getLocalImages(contentResolver: ContentResolver): MutableList<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.TITLE
        )
        contentResolver.query(collection, projection, "", arrayOf(), "")?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                val title = cursor.getString(titleColumn)
                val infos = title.split("_")
                val image = MediaStoreImage(id, displayName, contentUri, infos.getOrNull(0), infos.getOrNull(1))

                images += image
            }
        }

        return images


    }

}