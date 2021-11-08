package com.example.learnspanishwithcrys.repositories

import com.example.learnspanishwithcrys.data.model.ResultMatch
import com.example.learnspanishwithcrys.data.model.Word
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
    val words = mutableListOf<Word>()
    init {
        getWords()
    }

    fun addResult(result: ResultMatch) {
        rankingCollection.add(result)
    }

    private fun getWords() {
        CoroutineScope(Dispatchers.IO).launch {
            val query = wordCollection
                .get()
                .await()
            try {
                if (query.isEmpty) {
                    return@launch
                }
                for(document in query) {
                    val word = document.toObject<Word>()
                    words.add(word)
                }
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }
}