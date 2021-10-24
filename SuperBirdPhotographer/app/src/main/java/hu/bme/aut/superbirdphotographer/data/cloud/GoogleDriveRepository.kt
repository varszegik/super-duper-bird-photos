package hu.bme.aut.superbirdphotographer.data.cloud

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.Scopes

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import java.util.*
import com.google.api.client.json.gson.GsonFactory

import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import android.util.Log

import com.google.api.client.http.FileContent





class GoogleDriveRepository(context: Context) : CloudImagesRepository {

    private val googleSignInAccount: GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(context)
    private val credential: GoogleAccountCredential = GoogleAccountCredential.usingOAuth2(
        context, Collections.singleton(Scopes.DRIVE_FILE)
    )
    private lateinit var googleDriveService: Drive
    init {
        if(googleSignInAccount != null){
            credential.selectedAccount = googleSignInAccount.account
            googleDriveService = Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                GsonFactory(),
                credential
            ).setApplicationName("GoogleDriveIntegration 3").build()
        }
    }

    private fun listDirectories(): MutableList<File>? {
            val result = googleDriveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder'")
                .setSpaces("drive")
                .setFields("nextPageToken, files(id,name)")
                .setPageToken(null)
                .execute()
            val list = result.files
            Log.d("GoogleDriveRepository", result.toString())
        return list
    }

    private fun createFolder(folderName: String): String? {
        val metadata = File()
            .setParents(Collections.singletonList("root"))
            .setMimeType("application/vnd.google-apps.folder")
            .setName(folderName)
        val googleFile: File = googleDriveService.files().create(metadata).execute()
        return googleFile.id
    }


    override fun uploadImage(file: java.io.File, folder: String, fileType: String) {
        if (googleSignInAccount != null) {
            var birdPhotographyDirectoryId = listDirectories()?.find { it -> it.name == folder }?.id
            if(birdPhotographyDirectoryId == null){
                birdPhotographyDirectoryId = createFolder(folder)
            }
            val metadata: File = File()
                .setParents(Collections.singletonList(birdPhotographyDirectoryId))
                .setMimeType(fileType)
                .setName(file.name)
            val fileContent = FileContent(fileType, file)
            googleDriveService.files().create(metadata, fileContent).execute()
        }
    }
}