package com.elearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.elearningapp.ui.theme.ELearningAppTheme
import com.elearningapp.ui.views.screens.content.Books
import com.elearningapp.ui.views.screens.content.LessonList
import com.elearningapp.ui.views.screens.content.PopularLessons
import com.elearningapp.ui.views.screens.content.VideoLesson
import com.elearningapp.ui.views.screens.content.VideoLessons
import com.elearningapp.ui.views.screens.dashboard.AboutUsScreen
import com.elearningapp.ui.views.screens.dashboard.Dashboard
import com.elearningapp.ui.views.screens.splashscreen.SplashScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ELearningAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FirebaseApp.initializeApp(this)
                    val navController = rememberNavController()
                    val context = applicationContext
                    val lifecycleOwner = this
                    NavHost(navController = navController, startDestination = "splash_screen") {

                        composable("splash_screen") {
                            SplashScreen(navController)
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
                            AboutUsScreen()
                        }
                    }
                }
            }
        }
    }
}
