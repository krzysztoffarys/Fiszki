package com.example.learnspanishwithcrys.repositories

import com.example.learnspanishwithcrys.data.model.ResultMatch
import com.example.learnspanishwithcrys.data.model.Word
import com.google.firebase.firestore.CollectionReference


class Repository(
    private val rankingCollection: CollectionReference
) {

    fun addResult(result: ResultMatch) {
        rankingCollection.add(result)
    }

    private val words = mutableListOf(
        Word("0001", "charakter","el charakteru", "https://firebasestorage.googleapis.com/v0/b/fiszki-989b7.appspot.com/o/0001.mp3?alt=media&token=07cfabbb-142a-4b9f-9109-9d2bb8dce9d9"),
        Word("0002", "dobry, grzeczny","bueno", "https://firebasestorage.googleapis.com/v0/b/fiszki-989b7.appspot.com/o/0002.mp3?alt=media&token=65e6caf1-8f4b-4548-a8ff-56a72ec56582"),
        Word("0003", "zły, niegrzeczny", "malo", "https://firebasestorage.googleapis.com/v0/b/fiszki-989b7.appspot.com/o/0003.mp3?alt=media&token=df165558-afce-426f-b40d-5dda29a40f1c"),
        Word("0004", "uprzejmy, miły","amable"),
        Word("0005", "ordynarny, grubiański", "grosero"),
        Word("0006", "sympatyczny","simpático")
    )

    fun provideWords(size: Int) : List<Word> {
        val list = mutableListOf<Word>()
        words.shuffle()
        for (i in 0 until size) {
            list.add(words[i])
        }
        return list
    }
}