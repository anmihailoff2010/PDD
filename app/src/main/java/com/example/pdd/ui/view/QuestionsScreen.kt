package com.example.pdd.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pdd.R
import com.example.pdd.ui.viewmodel.QuestionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsScreen(navController: NavHostController, category: String, ticketNumber: Int, viewModel: QuestionsViewModel) {
    val context = LocalContext.current
    val notoSans = FontFamily(
        Font(R.font.noto_sans_regular),
        Font(R.font.noto_sans_bold, FontWeight.Bold)
    )

    LaunchedEffect(ticketNumber) {
        viewModel.loadQuestions(ticketNumber)
    }

    if (viewModel.questions.isNotEmpty()) {
        val question = viewModel.questions[viewModel.currentQuestionIndex]
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Вопросы для категории $category",
                            fontFamily = notoSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Left
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFFAFAFA),
                        titleContentColor = Color(0xFF39434F)
                    )
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFFFAFAFA))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        question.imageUrl?.let { imageUrl ->
                            val painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context)
                                    .data(imageUrl)
                                    .build()
                            )
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(327.dp)
                                    .height(123.dp)
                                    .clip(RoundedCornerShape(14.dp, 14.dp, 14.dp, 14.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = question.text,
                            fontFamily = notoSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Left,
                            color = Color(0xFF606873),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        question.answers.forEachIndexed { index, answer ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectAnswer(index)
                                        if (viewModel.currentQuestionIndex < viewModel.questions.size - 1) {
                                            viewModel.nextQuestion()
                                        } else {
                                            viewModel.calculateMistakes()
                                            navController.navigate("test_results")
                                        }
                                    },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${index + 1}. ${answer.answerText}",
                                        fontFamily = notoSans,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        lineHeight = 20.sp,
                                        textAlign = TextAlign.Left,
                                        color = Color(0xFF39434F)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
