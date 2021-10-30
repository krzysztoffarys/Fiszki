package com.example.learnspanishwithcrys.ui.match

import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.other.Constants.delayMatch
import com.example.learnspanishwithcrys.other.Constants.matchGameDuration
import com.example.learnspanishwithcrys.other.Constants.timeMatch
import com.example.learnspanishwithcrys.other.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MatchViewModel @Inject constructor() : ViewModel() {

    private var _selectedTextView: TextView? = null
    private var isSelected = false
    private var isWorking = false
    private var wordLeft = 12
    private var counter = 0.0
    //
    private val _time = MutableLiveData<String>()
    val time: LiveData<String> = _time
    private val _colorTitle = MutableLiveData<Int>()
    val colorTitle: LiveData<Int> = _colorTitle
    //
    private val _gameStatus = MutableLiveData<Game>()
    val gameStatus: LiveData<Game> = _gameStatus

    init {
        startTimeCounter()
        _gameStatus.postValue(Game.ongoing(null))
        _colorTitle.postValue(Color.BLACK)
    }
    private val _words = mutableListOf(
        Word("0001", "el\ncharakteru", "charakter"),
        Word("0002", "bueno", "dobry,\ngrzeczny"),
        Word("0003", "malo", "zły,\nniegrzeczny"),
        Word("0004", "amable", "uprzejmy,\nmiły"),
        Word("0005", "grosero", "ordynarny,\ngrubiański"),
        Word("0006", "simpático", "sympatyczny"),
    )

    val words = mutableListOf<String>()
    val onClickListener = View.OnClickListener { textView ->
        if (isWorking) {
            return@OnClickListener
        }
        if(!isSelected) {
            textView.setBackgroundColor(Color.YELLOW)
            isSelected = true
            _selectedTextView = textView as TextView
            return@OnClickListener
        }
        if (_selectedTextView != null) {
            isWorking = true
            //if we click on the same button
            if (_selectedTextView == textView) {
                textView.setBackgroundColor(Color.WHITE)
                reset()
            } else {
                //if not then we check if that 2 words means that same
                val selectedTextView = _selectedTextView!!
                val text1 = selectedTextView.text.toString()
                val text2 = (textView as TextView).text.toString()
                if (meansTheSame(text1, text2)) {
                    //good anwser
                    textView.setBackgroundColor(Color.GREEN)
                    selectedTextView.setBackgroundColor(Color.GREEN)
                    counter -= timeMatch
                    _colorTitle.postValue(Color.GREEN)
                    viewModelScope.launch {
                        delay(delayMatch)
                        textView.visibility = View.GONE
                        selectedTextView.visibility = View.GONE
                        wordLeft -= 2
                        if (wordLeft == 0) {
                            _gameStatus.postValue(Game.win(((counter * 10).roundToInt().toDouble() / 10).toString()))
                        }
                        reset()
                    }

                } else {
                    //bad answer
                    textView.setBackgroundColor(Color.RED)
                    selectedTextView.setBackgroundColor(Color.RED)
                    counter += timeMatch
                    _colorTitle.postValue(Color.RED)
                    viewModelScope.launch {
                        delay(delayMatch)
                        textView.setBackgroundColor(Color.WHITE)
                        selectedTextView.setBackgroundColor(Color.WHITE)
                        reset()
                    }
                }
            }
        }

    }

    fun prepareWords() {
        for (i in _words.indices) {
            words.add(_words[i].polish)
            words.add(_words[i].spanish)
        }
        words.shuffle()
    }

    private fun meansTheSame(text1: String, text2: String) : Boolean {
        for (i in _words) {
            if ((i.polish == text1 && i.spanish == text2) || (i.polish == text2 && i.spanish == text1)) {
                return true
            }
        }
        return false
    }

    private fun reset() {
        isSelected = false
        _selectedTextView = null
        isWorking = false
        _colorTitle.postValue(Color.BLACK)
    }

    private fun startTimeCounter() {
        object : CountDownTimer(90000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                counter += 0.1
                if (counter > matchGameDuration) {
                    Timber.d("halo")
                    _gameStatus.postValue(Game.lose(null))
                }
                _time.postValue("${(counter * 10).roundToInt().toDouble() / 10} seconds")
            }
            override fun onFinish() {
                _gameStatus.postValue(Game.lose(null))
            }
        }.start()
    }
}