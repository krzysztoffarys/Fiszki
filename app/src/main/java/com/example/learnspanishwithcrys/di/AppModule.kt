package com.example.learnspanishwithcrys.di

import com.example.learnspanishwithcrys.other.Constants.RANKING_FIREBASE
import com.example.learnspanishwithcrys.repositories.Repository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore

    @Provides
    fun provideRankingCollection(fireStore: FirebaseFirestore) = fireStore.collection(RANKING_FIREBASE)

    @Singleton
    @Provides
    fun provideRepository(rankingCollection: CollectionReference) = Repository(rankingCollection)
}