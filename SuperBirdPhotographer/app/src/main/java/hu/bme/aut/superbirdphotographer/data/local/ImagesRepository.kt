package hu.bme.aut.superbirdphotographer.data.local

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.query
import android.provider.MediaStore.Images.Thumbnails.query
import android.provider.MediaStore.Video.query
import androidx.recyclerview.widget.DiffUtil
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class MediaStoreImage(
    val id: Long,
    val displayName: String,
    val contentUri: Uri,
    val species: String?,
    val date: Date?
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
                val species =  infos.getOrNull(0)
                val dateString = infos.getOrNull(1)
                val image = MediaStoreImage(id, displayName, contentUri,species, parseFileNameDate(FILENAME, dateString))

                images += image
            }
        }

        return images


    }

    fun getRealPathFromURI(contentResolver: ContentResolver, contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    companion object {
        const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        const val IMAGES_SUBDIRECTORY = "BirdPhotography"


        fun generateFileName(format: String, extension: String): String {
            return SimpleDateFormat(format, Locale.GERMAN)
                .format(System.currentTimeMillis()) + extension
        }

        fun parseFileNameDate(format: String, source: String?): Date? {
            return SimpleDateFormat(format, Locale.GERMAN).parse(source)
        }
    }

}