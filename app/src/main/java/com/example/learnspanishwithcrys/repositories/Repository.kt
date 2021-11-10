package com.example.learnspanishwithcrys.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnspanishwithcrys.data.model.Category
import com.example.learnspanishwithcrys.data.model.ResultMatch
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.other.Constants.WORDS_FIREBASE
import com.example.learnspanishwithcrys.other.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class Repository(
    private val rankingCollection: CollectionReference,
    private val fireStore: FirebaseFirestore
) {

    private val _wordsStatus = MutableLiveData<Resource<List<Word>>>()
    val wordsStatus: LiveData<Resource<List<Word>>> = _wordsStatus

    private val _curCategory = MutableLiveData<Category>()
    val curCategory: LiveData<Category> = _curCategory

    val words = mutableListOf<Word>()
    init {
        getWords(Category("Charakter i osobowość", 12, WORDS_FIREBASE))
    }

    fun addResult(result: ResultMatch) {
        rankingCollection.add(result)
    }

    fun getWords(category: Category) {
        _curCategory.postValue(category)
        val collection = fireStore.collection(category.collectionName)
        CoroutineScope(Dispatchers.IO).launch {
            _wordsStatus.postValue(Resource.loading(null))
            val query = collection
                .get()
                .await()
            try {
                if (query.isEmpty) {
                    _wordsStatus.postValue(Resource.error("An unknown error",null))
                    return@launch
                }
                val list = mutableListOf<Word>()
                for(document in query) {
                    val word = document.toObject<Word>()
                    list.add(word)
                }
                _wordsStatus.postValue(Resource.success(list))
            } catch (e: Exception) {
                _wordsStatus.postValue(Resource.error(e.message?: "An unknown error",null))
                Timber.d(e)
            }
        }
    }

    val categories = listOf(
        Category("Charakter i osobowość", 12, "charakteriosobowosc"),
        Category("Czas", 38, "czas"),
        Category("Czesci ciała", 25, "czesciciala"),
        Category("Dom", 34,"dom"),
        )
}