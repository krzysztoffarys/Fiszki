package com.example.learnspanishwithcrys.ui.flashcard

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnspanishwithcrys.data.model.Word
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel
@Inject constructor(
    sharedPref: SharedPreferences
): ViewModel() {

    lateinit var words: List<Word>
    var isPolish = true
    private val _selectedWordId = MutableLiveData<Int>()
    val selectedWordId: LiveData<Int> = _selectedWordId

    private val _knownWords = MutableLiveData<Int>()
    val knownWords: LiveData<Int> = _knownWords
    private val _unknownWords = MutableLiveData<Int>()
    val unknownWords: LiveData<Int> = _unknownWords

    var correctAnswers = 0
    var incorrectAnswers = 0
    var answers = mutableListOf<Boolean>()

    val options = arrayOf("Definicja", "Określenie", "Oba")
    var selectedOption = sharedPref.getInt("flashcardOption", 0)

    init {
        optionsChange()
    }


    fun nextWord() {
        _selectedWordId.postValue(selectedWordId.value?.plus(1))
    }

    fun previousWord() {

        if(_selectedWordId.value!! <= 0) {
            return
        }
        _selectedWordId.postValue(selectedWordId.value?.minus(1))
        if (answers[answers.size - 1]) {
            _knownWords.postValue(knownWords.value?.minus(1))
            answers.removeAt(answers.size - 1)
            correctAnswers--
        } else {
            _unknownWords.postValue(unknownWords.value?.minus(1))
            answers.removeAt(answers.size - 1)
            incorrectAnswers--
        }
    }

    fun isTheWordKnown(status: Boolean) {
        if (status) {
            _knownWords.postValue(knownWords.value?.plus(1))
            correctAnswers++
            answers.add(true)
        } else {
            _unknownWords.postValue(unknownWords.value?.plus(1))
            incorrectAnswers++
            answers.add(false)
        }
    }

    fun optionsChange() {
        if (selectedOption == 0) {
            isPolish = true
        } else if (selectedOption == 1) {
            isPolish = false
        }
        _selectedWordId.postValue(0)
        _knownWords.postValue(0)
        _unknownWords.postValue(0)

        correctAnswers = 0
        incorrectAnswers = 0
        answers = mutableListOf()
    }

}