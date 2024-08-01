package com.example.pdd.ui.view

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.pdd.R
import com.example.pdd.ui.model.Question
import com.example.pdd.ui.viewmodel.QuestionsViewModel
import com.example.pdd.ui.viewmodel.QuestionsViewModelFactory
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PddApp()
        }
    }
}

@Composable
fun PddApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "category_selection") {
        composable("category_selection") { CategorySelectionScreen(navController) }
        composable("questions/{category}/{ticketNumber}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("ticketNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            val ticketNumber = backStackEntry.arguments?.getInt("ticketNumber")
            if (category != null && ticketNumber != null) {
                QuestionsScreen(navController, category, ticketNumber)
            }
        }
        composable("test_results") { TestResultsScreen(navController) }
        composable("mistakes") { MistakesScreen(navController) }
    }
}

fun navigateToRandomTicket(navController: NavController, category: String) {
    val randomTicketNumber = (1..40).random()  // Example range for random ticket selection
    navController.navigate("questions/$category/$randomTicketNumber")
}

fun loadJsonFromAssets(context: Context, fileName: String): String {
    return try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charsets.UTF_8)
    } catch (e: IOException) {
        Log.e("loadJsonFromAssets", "Error reading $fileName: ${e.message}")
        ""
    }
}

fun parseQuestions(json: String): List<Question> {
    return try {
        val gson = Gson()
        val listType = object : TypeToken<List<Question>>() {}.type
        gson.fromJson(json, listType)
    } catch (e: Exception) {
        Log.e("parseQuestions", "Error parsing JSON: ${e.message}")
        emptyList()
    }
}




