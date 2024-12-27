package com.elearningapp.ui.views.screens.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elearningapp.R
import com.elearningapp.ui.theme.blue
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun Books(navController: NavController) {
    val scrollState = rememberScrollState()
    val imageResourceIds = listOf(
        R.drawable.bukufisika,
        R.drawable.bukubiologi,
        R.drawable.bukukimia
    )

    val selectedTab = remember { mutableStateOf(1) } // Default to Books tab

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                elevation = 8.dp
            ) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = if (selectedTab.value == 0) blue else Color.Gray
                        )
                    },
                    label = { androidx.compose.material.Text("Home", color = if (selectedTab.value == 0) blue else Color.Gray) },
                    selected = selectedTab.value == 0,
                    onClick = {
                        selectedTab.value = 0
                        navController.navigate("dashboard")
                    }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            Icons.Filled.Book,
                            contentDescription = "Books",
                            tint = if (selectedTab.value == 1) blue else Color.Gray
                        )
                    },
                    label = { androidx.compose.material.Text("Books", color = if (selectedTab.value == 1) blue else Color.Gray) },
                    selected = selectedTab.value == 1,
                    onClick = { selectedTab.value = 1 }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            Icons.Filled.Description,
                            contentDescription = "Papers",
                            tint = if (selectedTab.value == 2) blue else Color.Gray
                        )
                    },
                    label = { androidx.compose.material.Text("Papers", color = if (selectedTab.value == 2) blue else Color.Gray) },
                    selected = selectedTab.value == 2,
                    onClick = {
                        selectedTab.value = 2
                        val link = "https://drive.google.com/drive/folders/1eC3W8VhgiFHp6VY0LPkff4FX6QZY_Fbs?usp=drive_link"
                        navController.navigate(
                            "video_lesson/${
                                URLEncoder.encode(
                                    link,
                                    StandardCharsets.UTF_8.toString()
                                )
                            }"
                        )
                    }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = "About Us",
                            tint = if (selectedTab.value == 3) blue else Color.Gray
                        )
                    },
                    label = { androidx.compose.material.Text("About", color = if (selectedTab.value == 3) blue else Color.Gray) },
                    selected = selectedTab.value == 3,
                    onClick = {
                        selectedTab.value = 3
                        navController.navigate("about_us")
                    }
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .background(Color.White)
                    .verticalScroll(scrollState)
            ) {
                imageResourceIds.forEachIndexed { index, imageResId ->
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val link = when (index) {
                                    0 -> "https://drive.google.com/file/d/1UBA7qCZa6ldrcLhT5l_0hujgks1YEPUu/view?usp=drive_link"
                                    1 -> "https://drive.google.com/file/d/1St-x_fi4bOuhZF2ygCjP4_Lc3Tg-IY1g/view?usp=drive_link"
                                    else -> "https://drive.google.com/file/d/1FXpI2uOeXZXJgq_I94qMRsPEUiZj8sL7/view?usp=drive_link"
                                }
                                navController.navigate(
                                    "video_lesson/${
                                        URLEncoder.encode(
                                            link,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    }"
                                )
                            }
                            .height(400.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    )
}