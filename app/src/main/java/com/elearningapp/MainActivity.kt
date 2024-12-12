package com.elearningapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.elearningapp.ui.theme.ELearningAppTheme
import com.elearningapp.ui.views.screens.auth.GoogleSignInScreen
import com.elearningapp.ui.views.screens.content.*
import com.elearningapp.ui.views.screens.dashboard.AboutUsScreen
import com.elearningapp.ui.views.screens.dashboard.Dashboard
import com.elearningapp.ui.views.screens.splashscreen.SplashScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    // Google Sign-In Launcher
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java) // Safely get the account
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            ELearningAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash_screen") {

                        composable("splash_screen") {
                            SplashScreen(navController)
                        }

                        composable("login_screen") {
                            GoogleSignInScreen { startGoogleSignIn() }
                        }

                        composable("main_screen") {
                            Dashboard(navController)
                        }

                        composable("lessons/{subject}") { backStackEntry ->
                            val subject = backStackEntry.arguments?.getString("subject")
                            if (subject != null) {
                                LessonList(subject, navController)
                            }
                        }

                        composable("popular_lessons") {
                            PopularLessons(navController)
                        }

                        composable("ar_assets") {
                            ARModelList(navController)
                        }

                        composable("ar_screen/{asset}") { backStackEntry ->
                            val asset = backStackEntry.arguments?.getString("asset")
                            if (asset != null) {
                                ARScreen(asset, navController)
                            }
                        }

                        composable("video_lessons/{links}/{chapter}/{subject}/{chapterNumber}") { backStackEntry ->
                            val links = backStackEntry.arguments?.getString("links")?.split(",")?.map {
                                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                            }
                            val chapter = backStackEntry.arguments?.getString("chapter")
                            val subject = backStackEntry.arguments?.getString("subject")
                            val chapterNumber = backStackEntry.arguments?.getString("chapterNumber")

                            if (links != null && chapter != null && chapterNumber != null && subject != null) {
                                VideoLessons(links, chapter, subject, chapterNumber, navController)
                            }
                        }

                        composable("video_lesson/{link}") { backStackEntry ->
                            val link = backStackEntry.arguments?.getString("link")
                            if (link != null) {
                                VideoLesson(link)
                            }
                        }

                        composable("books") {
                            Books(navController)
                        }

                        composable("about_us") {
                            AboutUsScreen(navController) // Passing the navController here
                        }
                    }
                }
            }
        }
    }

    private fun startGoogleSignIn() {
        // Configure Google Sign-In
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Ensure this is in strings.xml
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

                    // Navigate to the main screen
                    setContent {
                        val navController = rememberNavController()
                        Surface(modifier = Modifier.fillMaxSize()) {
                            NavHost(navController = navController, startDestination = "main_screen") {
                                composable("main_screen") { Dashboard(navController) }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
