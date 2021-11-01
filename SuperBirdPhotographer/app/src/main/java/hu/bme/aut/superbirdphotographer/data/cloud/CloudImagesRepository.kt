package hu.bme.aut.superbirdphotographer.data.cloud

import com.google.api.services.drive.Drive

interface CloudImagesRepository {
    fun uploadImage(file: java.io.File, folder: String, fileType: String)
    fun sendNotification(label: String)
}