package com.example.learnspanishwithcrys.ui.write

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.other.Constants.DELAY
import com.example.learnspanishwithcrys.other.Game
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WriteViewModel : ViewModel() {

    val words = mutableListOf(
        Word("0001", "charakter", "el charakteru"),
        Word("0002", "dobry, grzeczny", "bueno"),
        Word("0003", "zły, niegrzeczny",  "malo"),
        Word("0004", "uprzejmy, miły", "amable", "uprzejmy, miły"),
        Word("0005", "ordynarny, grubiański", "grosero", "ordynarny, grubiański"),
        Word("0006", "sympatyczny", "simpático")
    )

    //
    var correctAnswers = 0
    var incorrectAnswers = 0
    private var isFirstAnswer = true

    //
    private val _selectedWordId = MutableLiveData<Int>()
    val selectedWordId: LiveData<Int> = _selectedWordId
    private val _answerStatus = MutableLiveData<Game>()
    val answerStatus: LiveData<Game> = _answerStatus
    //
    private val _buttonVisibility = MutableLiveData<Int>()
    val buttonVisibility: LiveData<Int> = _buttonVisibility
    //
    private val _answerVisibility = MutableLiveData<Int>()
    val answerVisibility: LiveData<Int> = _answerVisibility

    init {
        _answerStatus.postValue(Game.ongoing(null))
        _selectedWordId.postValue(0)
        reset()
    }

    val onClickListener = View.OnClickListener {
        _answerVisibility.postValue(View.VISIBLE)
        _buttonVisibility.postValue(View.GONE)
    }


    fun checkAnswer(answer: String) = viewModelScope.launch {
        _answerStatus.postValue(Game.ongoing(null))
        if (words[selectedWordId.value!!].spanish == answer) {
            if(isFirstAnswer) {
                correctAnswers++
                isFirstAnswer = false
            }
            _answerStatus.postValue(Game.win(null))
            delay(DELAY)
            _answerStatus.postValue(Game.ongoing(null))
            _selectedWordId.postValue(_selectedWordId.value?.plus(1))
            reset()
        } else {
            if(isFirstAnswer) {
                incorrectAnswers++
                isFirstAnswer = false
            }
            _answerStatus.postValue(Game.lose(null))
            _answerVisibility.postValue(View.VISIBLE)
            _buttonVisibility.postValue(View.GONE)
            delay(DELAY)
            _answerStatus.postValue(Game.ongoing(null))
        }
    }

    private fun reset() {
        _answerVisibility.postValue(View.GONE)
        _buttonVisibility.postValue(View.VISIBLE)
        _answerStatus.postValue(Game.ongoing(null))
        isFirstAnswer = true
    }

}