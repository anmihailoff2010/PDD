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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pdd.ui.viewmodel.QuestionsViewModel
import com.example.pdd.ui.viewmodel.QuestionsViewModelFactory

@Composable
fun TestResultsScreen(navController: NavHostController) {
    val context = LocalContext.current.applicationContext as Application
    val viewModel: QuestionsViewModel = viewModel(factory = QuestionsViewModelFactory(context))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Итоги теста") }
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
                    text = "Количество правильных ответов: ${viewModel.correctAnswersCount}/${viewModel.totalQuestionsCount}",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { navController.navigate("mistakes") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Мои ошибки")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    viewModel.reset()
                    navController.navigate("category_selection")
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Повторить тест")
                }
            }
        }
    )
}