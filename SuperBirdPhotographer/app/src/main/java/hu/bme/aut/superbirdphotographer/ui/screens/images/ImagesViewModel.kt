package hu.bme.aut.superbirdphotographer.ui.screens.images

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.superbirdphotographer.data.local.ImagesRepository
import hu.bme.aut.superbirdphotographer.data.local.MediaStoreImage
import javax.inject.Inject

@HiltViewModel
@Module
@InstallIn(ViewModelComponent::class)
class ImagesViewModel @Inject constructor() : ViewModel() {
    private val imagesRepository = ImagesRepository()


    fun listImages(contentResolver: ContentResolver): MutableList<MediaStoreImage> {
        return imagesRepository.getLocalImages(contentResolver)
    }

}