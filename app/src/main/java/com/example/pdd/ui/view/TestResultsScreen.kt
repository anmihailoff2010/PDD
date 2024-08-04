package com.example.pdd.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pdd.R
import com.example.pdd.ui.viewmodel.QuestionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestResultsScreen(navController: NavHostController, viewModel: QuestionsViewModel) {
    val context = LocalContext.current
    val notoSans = FontFamily(
        Font(R.font.noto_sans_regular),
        Font(R.font.noto_sans_bold, FontWeight.Bold)
    )

    LaunchedEffect(Unit) {
        viewModel.calculateMistakes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Итоги теста",
                        fontFamily = notoSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 22.sp,
                        color = Color(0xFF39434F)
                    )
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Количество правильных",
                                fontFamily = notoSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color(0xFF39434F)
                            )
                            Text(
                                text = "ответов",
                                fontFamily = notoSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color(0xFF39434F)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${viewModel.correctAnswersCount}/${viewModel.totalQuestionsCount()}",
                                fontFamily = notoSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color(0xFF39434F),
                                modifier = Modifier
                                    .padding(bottom = 6.dp)
                                    .height(32.dp)
                            )
                            LinearProgressIndicator(
                                progress = if (viewModel.totalQuestionsCount() > 0)
                                    viewModel.correctAnswersCount / viewModel.totalQuestionsCount().toFloat()
                                else 0f,
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(0xFF1B85F3),
                                trackColor = Color(0xFFD9DFE6)
                            )
                        }
                    }
                }

                // "Мои ошибки" as a Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(bottom = 12.dp)
                        .clickable { navController.navigate("mistakes") },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Мои ошибки",
                            fontFamily = notoSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF39434F)
                        )
                    }
                }

                // "Повторить тест" as a Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            viewModel.reset() // Reset test data
                            navController.navigate("category_selection")
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Повторить тест",
                            fontFamily = notoSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF39434F)
                        )
                    }
                }
            }
        }
    )
}
