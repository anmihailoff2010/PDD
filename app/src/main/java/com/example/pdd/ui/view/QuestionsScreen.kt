package com.example.pdd.ui.view

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.pdd.ui.viewmodel.QuestionsViewModel
import com.example.pdd.ui.viewmodel.QuestionsViewModelFactory

@Composable
fun QuestionsScreen(navController: NavHostController, category: String, ticketNumber: Int) {
    val context = LocalContext.current.applicationContext as Application
    val viewModel: QuestionsViewModel = viewModel(factory = QuestionsViewModelFactory(context))

    LaunchedEffect(ticketNumber) {
        viewModel.loadQuestions(ticketNumber)
    }

    if (viewModel.questions.isNotEmpty()) {
        val question = viewModel.questions[viewModel.currentQuestionIndex]
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Вопросы для категории $category") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("category_selection") }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    question.imageUrl?.let {
                        val painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(it)
                                .apply {
                                    size(coil.size.Size.ORIGINAL)
                                    scale(Scale.FILL)
                                }
                                .build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.78f)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = question.text,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    question.answers.forEachIndexed { index, answer ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = viewModel.userAnswers.any { it.questionId == question.id && it.selectedAnswer == index },
                                onClick = {
                                    Log.d("QuestionsScreen", "Question ID: ${question.id}, Answer Index: $index")
                                    viewModel.selectAnswer(index)
                                }
                            )
                            Text(answer.answerText)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (viewModel.currentQuestionIndex < viewModel.questions.size - 1) {
                                viewModel.nextQuestion()
                            } else {
                                viewModel.calculateMistakes()
                                navController.navigate("test_results")
                            }
                        },
                        enabled = viewModel.userAnswers.any { it.questionId == question.id },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Далее")
                    }
                }
            }
        )
    } else {
        Text("Загрузка данных...", modifier = Modifier.fillMaxSize())
    }
}