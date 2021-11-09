package com.example.learnspanishwithcrys.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.other.Resource
import com.example.learnspanishwithcrys.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val wordsStatus: LiveData<Resource<List<Word>>> = repository.wordsStatus
    lateinit var words : List<Word>

    //write functionality
    var writeCorrectAnswers = 0
    var writeIncorrectAnswers = 0
    var writeAnswers = mutableListOf<Boolean>()

    fun newWrite() {
        writeCorrectAnswers = 0
        writeIncorrectAnswers = 0
    }

    //flashcard
    var flashcardCorrectAnswers = 0
    var flashcardIncorrectAnswers = 0
    var flashcardAnswers = mutableListOf<Boolean>()

    fun newFlashcard() {
        flashcardCorrectAnswers = 0
        flashcardIncorrectAnswers = 0
    }
}