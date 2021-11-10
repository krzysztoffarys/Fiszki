package com.example.learnspanishwithcrys.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.learnspanishwithcrys.data.model.Category
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.other.Resource
import com.example.learnspanishwithcrys.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val wordsStatus: LiveData<Resource<List<Word>>> = repository.wordsStatus
    val curCategory: LiveData<Category> = repository.curCategory
    private val allCategories = repository.categories

    lateinit var words : List<Word>

    var isLoading = true

    fun loadNewCategory(category: Category) {
        repository.getWords(category)
    }

    //write functionality
    var writeCorrectAnswers = 0
    var writeIncorrectAnswers = 0
    var writeAnswers = mutableListOf<Boolean>()

    lateinit var writeWords: List<Word>

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

    //categories
    fun provideCategories() : List<Category> {
        val list = mutableListOf<Category>()
        for (i in allCategories) {
            if (i != curCategory.value)
                list.add(i)
        }
        return list
    }


}