package com.example.pdd.ui.model

import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("answer_text") val answerText: String,
    @SerializedName("is_correct") val isCorrect: Boolean
)

