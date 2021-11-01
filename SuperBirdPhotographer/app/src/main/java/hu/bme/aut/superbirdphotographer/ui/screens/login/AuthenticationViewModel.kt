package hu.bme.aut.superbirdphotographer.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
) : ViewModel() {
    val signInRequestCode = 1


    fun checkPreviouslySigned(context: Context, previouslySignedCallback: (account: GoogleSignInAccount) -> Unit) {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if(account != null){
            Firebase.messaging.subscribeToTopic(account.id!!)
            previouslySignedCallback(account)
        }
    }


}