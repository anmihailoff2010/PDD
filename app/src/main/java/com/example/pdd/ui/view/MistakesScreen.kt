package com.example.pdd.ui.view

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pdd.ui.viewmodel.QuestionsViewModel
import com.example.pdd.ui.viewmodel.QuestionsViewModelFactory

@Composable
fun MistakesScreen(navController: NavHostController) {
    val context = LocalContext.current.applicationContext as Application
    val viewModel: QuestionsViewModel = viewModel(factory = QuestionsViewModelFactory(context))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои ошибки") },
                backgroundColor = MaterialTheme.colors.primary
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
                Text(
                    text = "Правильных ответов: ${viewModel.correctAnswersCount}/${viewModel.totalQuestionsCount}",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (viewModel.mistakes.isEmpty()) {
                    Text("Нет ошибок")
                } else {
                    viewModel.mistakes.forEach { mistake ->
                        val question = viewModel.questions.find { it.id == mistake.questionId }
                        question?.let {
                            Text(
                                text = it.text,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            it.answers.forEachIndexed { index, answer ->
                                val isCorrectAnswer = answer.isCorrect
                                val userSelected = index == mistake.selectedAnswer

                                val color = when {
                                    userSelected && isCorrectAnswer -> Color.Green
                                    userSelected -> Color.Red
                                    isCorrectAnswer -> Color.Green
                                    else -> Color.Gray
                                }

                                Text(
                                    text = answer.answerText,
                                    color = color,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.reset()
                        navController.navigate("category_selection")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Вернуться к категориям")
                }
            }
        }
    )
}