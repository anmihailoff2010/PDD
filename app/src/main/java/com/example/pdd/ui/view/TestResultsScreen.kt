package com.example.pdd.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pdd.ui.viewmodel.QuestionsViewModel
import com.example.pdd.ui.viewmodel.QuestionsViewModelFactory

@Composable
fun TestResultsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: QuestionsViewModel = viewModel(factory = QuestionsViewModelFactory(context))

    LaunchedEffect(Unit) {
        viewModel.calculateMistakes()
    }

    // Добавление лога для отладки
    Log.d("TestResultsScreen", "Correct Answers: ${viewModel.correctAnswersCount}, Total Questions: ${viewModel.totalQuestionsCount()}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Итоги теста") },
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
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Количество правильных ответов",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${viewModel.correctAnswersCount}/${viewModel.totalQuestionsCount()}",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = if (viewModel.totalQuestionsCount() > 0)
                        viewModel.correctAnswersCount / viewModel.totalQuestionsCount().toFloat()
                    else 0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate("mistakes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Мои ошибки")
                }
                Button(
                    onClick = {
                        viewModel.reset()
                        navController.navigate("category_selection")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Повторить тест")
                }
            }
        }
    )
}

