package hu.bme.aut.superbirdphotographer.data.cloud

interface CloudImagesRepository {
    fun uploadImage(file: java.io.File, folder: String, fileType: String)
    fun sendNotification(label: String)
}