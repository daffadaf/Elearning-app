package com.elearningapp.ui.views.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elearningapp.R
import com.elearningapp.ui.theme.blue
import com.elearningapp.ui.theme.lightBlue

@Composable
fun AboutUsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(3) } // Default tab for About Us is 3.

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
                            tint = if (selectedTab == 0) blue else Color.Gray
                        )
                    },
                    label = { Text("Home", color = if (selectedTab == 0) blue else Color.Gray) },
                    selected = selectedTab == 0,
                    onClick = {
                        if (selectedTab != 0) {
                            selectedTab = 0
                            navController.navigate("dashboard") { // Correct route here
                                popUpTo("dashboard") { inclusive = true } // Prevents multiple instances of the screen
                            }
                        }
                    }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            Icons.Filled.Book,
                            contentDescription = "Books",
                            tint = if (selectedTab == 1) blue else Color.Gray
                        )
                    },
                    label = { Text("Books", color = if (selectedTab == 1) blue else Color.Gray) },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("books")
                    }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            Icons.Filled.Description,
                            contentDescription = "Papers",
                            tint = if (selectedTab == 2) blue else Color.Gray
                        )
                    },
                    label = { Text("Papers", color = if (selectedTab == 2) blue else Color.Gray) },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        val link = "https://drive.google.com/drive/folders/1eC3W8VhgiFHp6VY0LPkff4FX6QZY_Fbs?usp=drive_link"
                        navController.navigate(
                            "video_lesson/${java.net.URLEncoder.encode(link, java.nio.charset.StandardCharsets.UTF_8.toString())}"
                        )
                    }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = "About Us",
                            tint = if (selectedTab == 3) blue else Color.Gray
                        )
                    },
                    label = { Text("About", color = if (selectedTab == 3) blue else Color.Gray) },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Text Judul
                Text(
                    text = stringResource(id = R.string.judul),
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Text Place
                Text(
                    text = stringResource(id = R.string.place),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Text Anggota Tim
                Text(
                    text = stringResource(id = R.string.anggotaTim),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Row for first two images
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kevin),
                        contentDescription = "About Us Image 1",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.genadi),
                        contentDescription = "About Us Image 2",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Row for next two images
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.daffa),
                        contentDescription = "About Us Image 3",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.rainhard),
                        contentDescription = "About Us Image 4",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button to navigate to LessonForm
                // Button to navigate to LessonForm with icon
                Button(
                    onClick = {
                        navController.navigate("LessonForm")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = lightBlue)
                ) {
                    // Add the Icon to the Button
                    Icon(
                        Icons.Filled.AdminPanelSettings,
                        contentDescription = "Halaman Admin",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Masuk Halaman Update Lesson")
                }


                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAboutUsScreen() {
    AboutUsScreen(navController = rememberNavController()) // Use rememberNavController for preview
}