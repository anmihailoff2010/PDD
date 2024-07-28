package com.example.pdd.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var totalQuestionsCount by mutableStateOf(20)

    fun loadQuestions(ticketNumber: Int) {
        val fileName = "ticket${ticketNumber}.json"
        try {
            val jsonString = loadJsonFromAsset(context, fileName)
            questions = parseQuestions(jsonString)
            reset()
        } catch (e: IOException) {
            Log.e("QuestionsViewModel", "Error loading questions from asset $fileName: ${e.message}")
        }
    }

    fun selectAnswer(answerIndex: Int) {
        val question = questions.getOrNull(currentQuestionIndex) ?: return
        val questionId = question.id
        val isCorrect = question.answers[answerIndex].isCorrect

        // Увеличиваем количество правильных ответов, если ответ верный
        if (isCorrect) {
            correctAnswersCount++
        } else {
            mistakes = mistakes + UserAnswer(questionId, answerIndex)
        }

        userAnswers = userAnswers.filter { it.questionId != questionId } + UserAnswer(questionId, answerIndex)
        Log.d("QuestionsViewModel", "Selected Answer: questionId=$questionId, answerIndex=$answerIndex, Is Correct: $isCorrect")
    }

    fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
        }
    }

    fun reset() {
        currentQuestionIndex = 0
        userAnswers = emptyList()
        mistakes = emptyList()
        correctAnswersCount = 0
    }

    fun calculateMistakes() {
        mistakes = userAnswers.filter { userAnswer ->
            val question = questions.find { it.id == userAnswer.questionId }
            question?.answers?.get(userAnswer.selectedAnswer)?.isCorrect == false
        }
        correctAnswersCount = userAnswers.count { userAnswer ->
            val question = questions.find { it.id == userAnswer.questionId }
            question?.answers?.get(userAnswer.selectedAnswer)?.isCorrect == true
        }
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
}


