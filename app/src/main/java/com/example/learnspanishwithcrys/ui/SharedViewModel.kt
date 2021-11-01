package com.example.learnspanishwithcrys.ui

import androidx.lifecycle.ViewModel
import com.example.learnspanishwithcrys.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {
    val words = repository.provideWords(6)

    //write functionality
    var writeCorrectAnswers = 0
    var writeIncorrectAnswers = 0
    var writeAnswers = mutableListOf<Boolean>()

    fun newWrite() {
        writeCorrectAnswers = 0
        writeIncorrectAnswers = 0
    }
}