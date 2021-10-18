package hu.bme.aut.superbirdphotographer.ui.screens.images

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.superbirdphotographer.data.local.ImagesRepository
import hu.bme.aut.superbirdphotographer.data.local.MediaStoreImage
import java.util.*
import javax.inject.Inject

@HiltViewModel
@Module
@InstallIn(ViewModelComponent::class)
class ImagesViewModel @Inject constructor() : ViewModel() {
    private val imagesRepository = ImagesRepository()


    fun listImages(contentResolver: ContentResolver): Map<Date?, List<MediaStoreImage>> {
        val images = imagesRepository.getLocalImages(contentResolver)
        val grouppedImages = images.groupBy { normalizeDate(it.date) }
        return grouppedImages
    }

    private fun normalizeDate(date: Date?): Date? {
        if (date != null) {

            val calendar = Calendar.getInstance()
            calendar.time = date

            val unroundedMinutes = calendar[Calendar.MINUTE]
            val mod = unroundedMinutes % 5
            calendar.add(Calendar.MINUTE, -mod)
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.time
        }
        return null
    }

}