package com.example.pdd.ui.model

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("title") val title: String,
    @SerializedName("ticket_number") val ticketNumber: String,
    @SerializedName("ticket_category") val ticketCategory: String,
    @SerializedName("image") val imageUrl: String?, // Ensure this contains the full URL
    @SerializedName("question") val text: String,
    @SerializedName("answers") val answers: List<Answer>,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("answer_tip") val answerTip: String,
    @SerializedName("topic") val topic: List<String>,
    @SerializedName("id") val id: String
)

