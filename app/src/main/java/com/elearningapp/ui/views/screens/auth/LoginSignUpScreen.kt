package com.elearningapp.ui.views.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginSignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (isLogin) "Login" else "Sign Up", color = Color.Black)

        Spacer(modifier = Modifier.height(20.dp))

        // Email input
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password input
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login/SignUp button
        Button(
            onClick = {
                if (isLogin) {
                    handleLogin(email, password, navController)
                } else {
                    handleSignUp(email, password, navController)
                }
            }
        ) {
            Text(text = if (isLogin) "Login" else "Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle between Login and SignUp
        TextButton(onClick = { isLogin = !isLogin }) {
            Text(text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login")
        }
    }
}

// Handle Login logic
fun handleLogin(email: String, password: String, navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("main_screen")
            } else {
                Toast.makeText(auth.app.applicationContext, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
}

// Handle Sign Up logic
fun handleSignUp(email: String, password: String, navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("main_screen")
            } else {
                Toast.makeText(auth.app.applicationContext, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginSignUpScreen(navController = rememberNavController())
}
