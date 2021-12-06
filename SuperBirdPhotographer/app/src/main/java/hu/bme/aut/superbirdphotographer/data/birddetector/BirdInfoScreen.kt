package hu.bme.aut.superbirdphotographer.data.birddetector

import android.graphics.Rect
import androidx.lifecycle.MutableLiveData

interface BirdInfoScreen {
    val imageRect: MutableLiveData<Rect?>

    fun takePicture(label: String)
}