package com.elearningapp.ui.views.screens.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elearningapp.R
import com.elearningapp.ui.theme.blue
import com.elearningapp.viewmodel.DashboardViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Dashboard(navController: NavController) {
    val viewModel: DashboardViewModel = viewModel()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
            BottomNavigationBar(selectedTab, navController) { newTab ->
                selectedTab = newTab
            }
        },
        content = { Content(navController) }
    )
}

@Composable
fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.padding(top = 5.dp, start = 15.dp, bottom = 5.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ruangsiswa),
                contentDescription = "Logo Image",
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = " Hello Learners!",
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ),
        )
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, navController: NavController, onTabSelected: (Int) -> Unit) {
    BottomNavigation(
        backgroundColor = Color.White,
        elevation = 8.dp
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home", tint = if (selectedTab == 0) blue else Color.Gray) },
            label = { Text("Home", color = if (selectedTab == 0) blue else Color.Gray) },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Book, contentDescription = "Books", tint = if (selectedTab == 1) blue else Color.Gray) },
            label = { Text("Books", color = if (selectedTab == 1) blue else Color.Gray) },
            selected = selectedTab == 1,
            onClick = {
                onTabSelected(1)
                navController.navigate("books")
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Description, contentDescription = "Papers", tint = if (selectedTab == 2) blue else Color.Gray) },
            label = { Text("Papers", color = if (selectedTab == 2) blue else Color.Gray) },
            selected = selectedTab == 2,
            onClick = {
                onTabSelected(2)
                val link = "https://drive.google.com/drive/folders/1wikaNfL8bHVrohvMbGXrsdufkBI1ZjBu?usp=drive_link"
                navController.navigate(
                    "video_lesson/${
                        URLEncoder.encode(link, StandardCharsets.UTF_8.toString())
                    }"
                )
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Info, contentDescription = "About", tint = if (selectedTab == 3) blue else Color.Gray) },
            label = { Text("About", color = if (selectedTab == 3) blue else Color.Gray) },
            selected = selectedTab == 3,
            onClick = {
                onTabSelected(3)
                navController.navigate("about_us")
            }
        )
    }
}

@Composable
fun Content(navController: NavController) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(navController)
        CourseList(navController)

        ContentText(first = "Popular Lessons", second = "See All", call = "popular", navController)
        CourseCard(navController)

        ContentText(first = "AR Learning", second = "See All", call = "ar", navController)
        AssessmentCard(navController)
    }
}
