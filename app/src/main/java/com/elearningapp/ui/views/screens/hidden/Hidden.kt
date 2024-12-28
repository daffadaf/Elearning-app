package com.elearningapp.ui.views.screens.hidden

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// Data class to represent your data model
data class Lesson(
    val subjectName: String,
    val lessonName: String,
    val numberOfLessons: String,
    val duration: String,
    val videoLinks: List<String>,
    val imageUrl: String,
    val threeDAssets: List<String>,
    val chapterNumber: String,
    val weightage: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonForm(
    subjectName: String,
    onSubjectNameChange: (String) -> Unit,
    lessonName: String,
    onLessonNameChange: (String) -> Unit,
    numberOfLessons: String,
    onNumberOfLessonsChange: (String) -> Unit,
    duration: String,
    onDurationChange: (String) -> Unit,
    videoLinks: List<String>,
    onVideoLinksChange: (List<String>) -> Unit,
    onSubmit: (Lesson) -> Unit,
    threeDAssets: List<String>,
    onThreeDAssetsChange: (List<String>) -> Unit,
    chapterNumber: String,
    onChapterNumberChange: (String) -> Unit,
    weightage: String,
    onWeightageChange: (String) -> Unit,
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val scrollState = rememberScrollState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Lesson Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = subjectName,
            onValueChange = onSubjectNameChange,
            label = { Text("Subject Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lessonName,
            onValueChange = onLessonNameChange,
            label = { Text("Lesson Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = chapterNumber,
            onValueChange = onChapterNumberChange,
            label = { Text("Chapter Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weightage,
            onValueChange = onWeightageChange,
            label = { Text("Weightage") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = numberOfLessons,
            onValueChange = onNumberOfLessonsChange,
            label = { Text("Number of Lessons") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = duration,
            onValueChange = onDurationChange,
            label = { Text("Duration") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Video Links",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        videoLinks.forEachIndexed { index, link ->
            OutlinedTextField(
                value = link,
                onValueChange = { newValue ->
                    val updatedLinks = videoLinks.toMutableList()
                    updatedLinks[index] = newValue
                    onVideoLinksChange(updatedLinks)
                },
                label = { Text("Video Link ${index + 1}") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val updatedLinks = videoLinks.toMutableList()
                updatedLinks.add("")
                onVideoLinksChange(updatedLinks)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add Video Link")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "3D Assets",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        threeDAssets.forEachIndexed { index, asset ->
            OutlinedTextField(
                value = asset,
                onValueChange = { newValue ->
                    val updatedAssets = threeDAssets.toMutableList()
                    updatedAssets[index] = newValue
                    onThreeDAssetsChange(updatedAssets)
                },
                label = { Text("3D Asset URL ${index + 1}") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val updatedAssets = threeDAssets.toMutableList()
                updatedAssets.add("")
                onThreeDAssetsChange(updatedAssets)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add 3D Asset")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
        }

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pick Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                imageUri?.let { uri ->
                    uploadImageToFirebase(uri) { imageUrl ->
                        val lesson = Lesson(
                            subjectName,
                            lessonName,
                            numberOfLessons,
                            duration,
                            videoLinks,
                            imageUrl,
                            threeDAssets,
                            chapterNumber,
                            weightage
                        )
                        onSubmit(lesson)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

private fun uploadImageToFirebase(uri: Uri, onComplete: (String) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val imagesRef = storageRef.child("lesson_images/${uri.lastPathSegment}")

    val uploadTask = imagesRef.putFile(uri)

    uploadTask.addOnSuccessListener {
        imagesRef.downloadUrl.addOnSuccessListener { uri ->
            onComplete(uri.toString())
        }
    }
}

@Composable
fun MyApp() {
    var subjectName by remember { mutableStateOf("") }
    var chapterNumber by remember { mutableStateOf("") }
    var weightage by remember { mutableStateOf("") }
    var lessonName by remember { mutableStateOf("") }
    var numberOfLessons by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var videoLinks by remember { mutableStateOf(listOf<String>()) }
    var threeDAssets by remember { mutableStateOf(listOf<String>()) }

    val context = LocalContext.current
    var submittedLesson by remember { mutableStateOf<Lesson?>(null) }

    submittedLesson?.let { lesson ->
        Firebase.database.reference.child("lessons").child(lesson.lessonName).setValue(lesson)
            .addOnSuccessListener {
                Toast.makeText(context, "Data uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to upload data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        submittedLesson = null
    }

    LessonForm(
        subjectName = subjectName,
        onSubjectNameChange = { subjectName = it },
        lessonName = lessonName,
        onLessonNameChange = { lessonName = it },
        numberOfLessons = numberOfLessons,
        onNumberOfLessonsChange = { numberOfLessons = it },
        duration = duration,
        onDurationChange = { duration = it },
        videoLinks = videoLinks,
        onVideoLinksChange = { videoLinks = it },
        threeDAssets = threeDAssets,
        onThreeDAssetsChange = { threeDAssets = it },
        chapterNumber = chapterNumber,
        onChapterNumberChange = { chapterNumber = it },
        weightage = weightage,
        onWeightageChange = { weightage = it },
        onSubmit = { submittedLesson = it }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLessonForm() {
    MyApp()
}