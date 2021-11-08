package com.example.learnspanishwithcrys.di

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.learnspanishwithcrys.other.Constants.RANKING_FIREBASE
import com.example.learnspanishwithcrys.other.Constants.SHARED_PREF
import com.example.learnspanishwithcrys.other.Constants.WORDS_FIREBASE
import com.example.learnspanishwithcrys.repositories.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    @Named("ranking")
    fun provideRankingCollection(fireStore: FirebaseFirestore) = fireStore.collection(RANKING_FIREBASE)

    @Provides
    @Named("word")
    fun provideWordCollection(fireStore: FirebaseFirestore) = fireStore.collection(WORDS_FIREBASE)

    @Singleton
    @Provides
    fun provideRepository(
        @Named("ranking") rankingCollection: CollectionReference,
        @Named("word") wordCollection: CollectionReference) =
        Repository(rankingCollection, wordCollection)

    @Singleton
    @Provides
    fun provideMediaPlayer() = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext
        context: Context): SharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
}