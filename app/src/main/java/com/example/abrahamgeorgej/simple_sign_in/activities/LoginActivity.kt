package com.example.abrahamgeorgej.simple_sign_in.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.abrahamgeorgej.simple_sign_in.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException


val RC_SIGN_IN = 9001

var account: GoogleSignInAccount? = null
var username: String? = null
var token: String? = null
var email: String? = null

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // sign in code based on https://developers.google.com/identity/sign-in/android/sign-in
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // the server_client_id, defined in strings, is retrieved from the Google API console credentials page
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    override fun onStart() {
        super.onStart()

        // sign in code based on https://developers.google.com/identity/sign-in/android/sign-in
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this)
        username = account?.displayName
        token = account?.idToken
        email = account?.email
        // Already signed in, move to explorer view.
        updateUI()

        // probably will need to add silent sign in functionality in this call
    }

    // sign in code based on https://developers.google.com/identity/sign-in/android/sign-in
    fun updateUI() {

        if (account != null) {
            val triggerMainActivityIntent = Intent(applicationContext, MainActivity::class.java)
            triggerMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            triggerMainActivityIntent.putExtra("account", account)
            applicationContext.startActivity(triggerMainActivityIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // sign in code based on https://developers.google.com/identity/sign-in/android/sign-in
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                account = task.getResult(ApiException::class.java)
                username = account?.displayName
                email = account?.email
                token = account?.idToken

                // Signed in successfully, move to explorer view.
                updateUI()

            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("LoginActivity", "signInResult:failed code=" + e.statusCode)
                updateUI()
            }
        }

    }

}