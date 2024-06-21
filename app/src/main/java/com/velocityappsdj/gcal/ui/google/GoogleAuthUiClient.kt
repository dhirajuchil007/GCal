package com.velocityappsdj.gcal.ui.google


import android.content.Context
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.velocityappsdj.gcal.common.Constants.WEB_CLIENT_ID


class GoogleAuthUiClient(private val context: Context, private val auth: FirebaseAuth) {
    companion object {
        private const val TAG = "GoogleAuthUiClient"
    }

    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(callback: () -> Unit) {
        val request = getCredentialRequest()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )
            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val signin = auth.signInWithCredential(firebaseCredential).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "signIn: Success ${googleIdToken}")
                    callback()

                }
            }

        } catch (e: Exception) {
            Log.d(TAG, "signIn: Error ${e.message}")
        }

    }

    private fun getCredentialRequest(): GetCredentialRequest {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .build()

        return GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
    }


}