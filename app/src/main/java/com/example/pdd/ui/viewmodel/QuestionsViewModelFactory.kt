package com.example.pdd.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class QuestionsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestionsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



