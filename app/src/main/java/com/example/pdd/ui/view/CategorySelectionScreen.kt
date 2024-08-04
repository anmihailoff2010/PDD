package com.example.pdd.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pdd.R
import com.example.pdd.ui.viewmodel.QuestionsViewModel
import androidx.compose.ui.text.style.TextAlign


@Composable
fun CategorySelectionScreen(navController: NavHostController, viewModel: QuestionsViewModel) {
    val notoSans = FontFamily(
        Font(R.font.noto_sans_regular),
        Font(R.font.noto_sans_bold, FontWeight.Bold)
    )

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFFAFAFA))
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Выбрать категорию",
                    style = TextStyle(
                        fontFamily = notoSans,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 22.sp,
                        color = Color(0xFF39434F),
                        textAlign = TextAlign.Left
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                val categories = listOf(
                    "A" to R.drawable.ic_motorcycle, // Replace with your actual drawable resource
                    "B" to R.drawable.ic_car // Replace with your actual drawable resource
                )
                categories.forEach { (category, iconRes) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { navigateToRandomTicket(navController, category) },
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(
                                        color = Color(0xFFD1E6FF).copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    tint = Color.Unspecified, // Ensure the icon is displayed in its original colors
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = "Категория $category",
                                style = TextStyle(
                                    fontFamily = notoSans,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600,
                                    lineHeight = 20.sp,
                                    color = Color(0xFF39434F),
                                    textAlign = TextAlign.Left
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}