package com.example.learnspanishwithcrys.repositories

import com.example.learnspanishwithcrys.data.model.ResultMatch
import com.google.firebase.firestore.CollectionReference


class Repository(
    private val rankingCollection: CollectionReference
) {

    fun addResult(result: ResultMatch) {
        rankingCollection.add(result)
    }

}