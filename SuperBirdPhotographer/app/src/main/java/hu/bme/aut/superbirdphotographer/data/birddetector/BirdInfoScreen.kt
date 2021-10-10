package hu.bme.aut.superbirdphotographer.data.birddetector

import android.graphics.Rect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface BirdInfoScreen {
    val imageRect: MutableLiveData<Rect?>

    fun takePicture()
}