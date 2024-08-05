package com.example.pdd.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.pdd.ui.viewmodel.QuestionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MistakesScreen(navController: NavHostController, viewModel: QuestionsViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.calculateMistakes()
    }

    val mistakes = viewModel.mistakes

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { navController.navigate("test_results") }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Мои ошибки",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF39434F)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFAFAFA)
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (mistakes.isEmpty()) {
                    Text("Нет ошибок")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        items(mistakes) { mistake ->
                            val question = viewModel.questions.find { it.id == mistake.questionId }
                            question?.let {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    val imageUrl = question.imageUrl
                                    if (!imageUrl.isNullOrBlank() && !imageUrl.contains("no_image.jpg")) {
                                        val painter = rememberAsyncImagePainter(
                                            ImageRequest.Builder(context)
                                                .data(imageUrl)
                                                .apply {
                                                    size(coil.size.Size.ORIGINAL)
                                                    scale(Scale.FILL)
                                                }
                                                .build()
                                        )
                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = painter,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .width(327.dp)
                                                    .height(123.dp)
                                                    .clip(
                                                        RoundedCornerShape(
                                                            14.dp,
                                                            14.dp,
                                                            14.dp,
                                                            14.dp
                                                        )
                                                    ),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }

                                    Text(
                                        text = it.text,
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        color = Color(0xFF606873),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                    )

                                    it.answers.forEachIndexed { index, answer ->
                                        val isCorrectAnswer = answer.isCorrect
                                        val userSelected = index == mistake.selectedAnswer

                                        val backgroundColor = when {
                                            userSelected && isCorrectAnswer -> Color(0xFF53C65F)
                                            userSelected -> Color(0xFFD9534F)
                                            isCorrectAnswer -> Color(0xFF53C65F)
                                            else -> Color(0xFFFFFFFF)
                                        }

                                        val textColor = when {
                                            userSelected && isCorrectAnswer -> Color(0xFFFFFFFF)
                                            userSelected -> Color(0xFFFFFFFF)
                                            isCorrectAnswer -> Color(0xFFFFFFFF)
                                            else -> Color(0xFF39434F)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .background(
                                                    color = backgroundColor,
                                                    shape = RoundedCornerShape(14.dp)
                                                )
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = answer.answerText,
                                                fontFamily = FontFamily.Default,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp,
                                                lineHeight = 20.sp,
                                                color = textColor,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }

                                    if (it.answerTip.isNotEmpty()) {
                                        Text(
                                            text = it.answerTip,
                                            fontFamily = FontFamily.Default,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            lineHeight = 15.sp,
                                            color = Color(0xFF606873),
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
