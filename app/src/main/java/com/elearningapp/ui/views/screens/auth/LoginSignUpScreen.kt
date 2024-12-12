package com.elearningapp.ui.views.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elearningapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginSignUpScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }

    // Get the context
    val context = androidx.compose.ui.platform.LocalContext.current

    // Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account, auth, navController, context)
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

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
                    startGoogleSignIn(googleSignInLauncher, context)
                } else {
                    handleSignUp(email, password, navController, context)
                }
            }
        ) {
            Text(text = if (isLogin) "Login with Google" else "Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle between Login and SignUp
        TextButton(onClick = { isLogin = !isLogin }) {
            Text(text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login")
        }
    }
}

// Start Google Sign-In
fun startGoogleSignIn(
    googleSignInLauncher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>,
    context: Context
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id)) // Replace with the actual default_web_client_id from strings.xml
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    googleSignInLauncher.launch(googleSignInClient.signInIntent)
}

// Handle Google Authentication with Firebase
fun firebaseAuthWithGoogle(
    account: GoogleSignInAccount?,
    auth: FirebaseAuth,
    navController: NavController,
    context: Context
) {
    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("main_screen")
            } else {
                Toast.makeText(context, "Authentication failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
}

// Handle Sign Up logic (if needed for email/password auth)
fun handleSignUp(
    email: String,
    password: String,
    navController: NavController,
    context: Context
) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("main_screen")
            } else {
                Toast.makeText(context, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginSignUpScreen(navController = rememberNavController())
}
