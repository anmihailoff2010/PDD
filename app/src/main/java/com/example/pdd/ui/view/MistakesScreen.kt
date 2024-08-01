package com.example.pdd.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pdd.ui.viewmodel.QuestionsViewModel
import com.example.pdd.ui.viewmodel.QuestionsViewModelFactory

@Composable
fun MistakesScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: QuestionsViewModel = viewModel(factory = QuestionsViewModelFactory(context))

    LaunchedEffect(Unit) {
        viewModel.calculateMistakes()
    }

    val mistakes = viewModel.mistakes

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои ошибки") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("test_results") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
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
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                if (mistakes.isEmpty()) {
                    Text("Нет ошибок")
                } else {
                    LazyColumn {
                        items(mistakes) { mistake ->
                            val question = viewModel.questions.find { it.id == mistake.questionId }
                            question?.let {
                                Column(modifier = Modifier.padding(vertical = 8.dp)) {
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

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        ) {
                                            RadioButton(
                                                selected = userSelected,
                                                onClick = null, // disable RadioButton interaction
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = color,
                                                    unselectedColor = color
                                                )
                                            )
                                            Text(
                                                text = answer.answerText,
                                                color = color,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }
                                    if (it.answerTip.isNotEmpty()) {
                                        Text(
                                            text = it.answerTip,
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 14.sp,
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

