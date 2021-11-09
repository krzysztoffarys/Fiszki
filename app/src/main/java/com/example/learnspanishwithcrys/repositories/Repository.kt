package com.example.learnspanishwithcrys.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnspanishwithcrys.data.model.ResultMatch
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.other.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class Repository(
    private val rankingCollection: CollectionReference,
    private val wordCollection: CollectionReference
) {

    private val _wordsStatus = MutableLiveData<Resource<List<Word>>>()
    val wordsStatus: LiveData<Resource<List<Word>>> = _wordsStatus

    val words = mutableListOf<Word>()
    init {
        getWords()
    }

    fun addResult(result: ResultMatch) {
        rankingCollection.add(result)
    }

    private fun getWords() {
        CoroutineScope(Dispatchers.IO).launch {
            _wordsStatus.postValue(Resource.loading(null))
            val query = wordCollection
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
}