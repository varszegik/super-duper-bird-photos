package hu.bme.aut.superbirdphotographer.data.cloud

import android.R.attr
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
import com.google.api.services.drive.model.FileList
import android.R.attr.mimeType

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


    override fun uploadImage(file: java.io.File, folder: String, fileType: String) {
        if (googleSignInAccount != null) {
            val metadata: File = File()
                .setParents(Collections.singletonList("root"))
                .setMimeType(fileType)
                .setName(file.name)
            val fileContent = FileContent(fileType, file)
            googleDriveService.files().create(metadata, fileContent).execute()
        }
    }
}