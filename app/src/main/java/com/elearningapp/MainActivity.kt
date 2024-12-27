package com.elearningapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elearningapp.ui.views.screens.auth.LoginScreen
import com.elearningapp.ui.views.screens.auth.SignUpScreen
import com.elearningapp.ui.views.screens.dashboard.Dashboard
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    // Google Sign-In Launcher
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            val navController = rememberNavController()

            Surface(modifier = Modifier.fillMaxSize()) {
                NavHost(navController = navController, startDestination = "signup_screen") {
                    // Navigation for SignUpScreen
                    composable("signup_screen") {
                        SignUpScreen(navController = navController)
                    }

                    // Navigation for LoginScreen
                    composable("login_screen") {
                        LoginScreen(navController = navController)
                    }
                    // Navigation for Dashboard
                    composable("dashboard") {
                        Dashboard(navController = navController)
                    }
                }
            }
        }
    }

    private fun startGoogleSignIn() {
        // Configure Google Sign-In
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, options)
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this, "Welcome ${user?.displayName}!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
