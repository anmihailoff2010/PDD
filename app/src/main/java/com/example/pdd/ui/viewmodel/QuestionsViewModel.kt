package com.example.pdd.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pdd.ui.model.Answer
import com.example.pdd.ui.model.Question
import com.example.pdd.ui.model.UserAnswer
import com.example.pdd.ui.view.loadJsonFromAssets
import com.example.pdd.ui.view.parseQuestions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class QuestionsViewModel(private val context: Context) : ViewModel() {
    private val baseUrl = "https://comandosfru.xyz/pdd"
    var questions: List<Question> by mutableStateOf(emptyList())
    var userAnswers: List<UserAnswer> by mutableStateOf(emptyList())
    var mistakes: List<UserAnswer> by mutableStateOf(emptyList())
    var currentQuestionIndex by mutableStateOf(0)
    var correctAnswersCount by mutableStateOf(0)

    init {
        loadMistakes()
        correctAnswersCount = countCorrectAnswers()
    }

    fun loadQuestions(ticketNumber: Int) {
        val fileName = "ticket${ticketNumber}.json"
        try {
            val jsonString = loadJsonFromAsset(context, fileName)
            questions = parseQuestions(jsonString)
            currentQuestionIndex = 0
            userAnswers = emptyList()
            mistakes = emptyList()
            correctAnswersCount = 0  // Reset correct answers count
            Log.d("QuestionsViewModel", "Questions loaded: ${questions.size}")
        } catch (e: IOException) {
            Log.e("QuestionsViewModel", "Error loading questions from asset $fileName: ${e.message}")
        }
    }

    fun selectAnswer(answerIndex: Int) {
        val question = questions.getOrNull(currentQuestionIndex) ?: return
        val questionId = question.id

        Log.d("QuestionsViewModel", "Selecting answer: $answerIndex for questionId: $questionId")

        // Update user answers and recalculate correct answers count
        userAnswers = userAnswers.filter { it.questionId != questionId } + UserAnswer(questionId, answerIndex)
        correctAnswersCount = countCorrectAnswers() // Recalculate correct answers count
    }

    fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            Log.d("QuestionsViewModel", "Moved to next question: index $currentQuestionIndex")
        } else {
            Log.d("QuestionsViewModel", "No more questions to move to.")
        }
    }

    fun reset() {
        currentQuestionIndex = 0
        userAnswers = emptyList()
        mistakes = emptyList()
        correctAnswersCount = 0
        Log.d("QuestionsViewModel", "Reset all values")
    }

    private fun countCorrectAnswers(): Int {
        return userAnswers.count { userAnswer ->
            val question = questions.find { it.id == userAnswer.questionId }
            val correctAnswerIndex = question?.answers?.indexOfFirst { it.isCorrect } ?: -1
            correctAnswerIndex == userAnswer.selectedAnswer
        }
    }

    fun totalQuestionsCount(): Int {
        return questions.size
    }

    fun calculateMistakes() {
        mistakes = userAnswers.filter { userAnswer ->
            val question = questions.find { it.id == userAnswer.questionId }
            val correctAnswerIndex = question?.answers?.indexOfFirst { it.isCorrect } ?: -1
            correctAnswerIndex != userAnswer.selectedAnswer
        }
        correctAnswersCount = countCorrectAnswers()
        saveMistakes()
        Log.d("QuestionsViewModel", "Mistakes calculated: $mistakes")
        Log.d("QuestionsViewModel", "Correct answers calculated: $correctAnswersCount")
    }

    private fun loadJsonFromAsset(context: Context, fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e("QuestionsViewModel", "Error reading file $fileName: ${e.message}")
            throw e
        }
    }

    private fun parseQuestions(json: String): List<Question> {
        return try {
            val gson = Gson()
            val listType = object : TypeToken<List<Question>>() {}.type
            val questions: List<Question> = gson.fromJson(json, listType)
            questions.map { question ->
                question.copy(imageUrl = question.imageUrl?.let { baseUrl + it.removePrefix(".") })
            }
        } catch (e: Exception) {
            Log.e("QuestionsViewModel", "Error parsing JSON: ${e.message}")
            emptyList()
        }
    }

    private fun saveMistakes() {
        val sharedPreferences = context.getSharedPreferences("pdd_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val mistakesJson = gson.toJson(mistakes)
        editor.putString("mistakes", mistakesJson)
        editor.apply()
        Log.d("QuestionsViewModel", "Mistakes saved: $mistakesJson")
    }

    private fun loadMistakes() {
        val sharedPreferences = context.getSharedPreferences("pdd_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val mistakesJson = sharedPreferences.getString("mistakes", "[]")
        mistakes = gson.fromJson(mistakesJson, object : TypeToken<List<UserAnswer>>() {}.type)
        Log.d("QuestionsViewModel", "Mistakes loaded: $mistakes")
    }
}


