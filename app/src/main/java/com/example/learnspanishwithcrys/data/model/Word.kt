package com.example.learnspanishwithcrys.data.model
import java.io.Serializable
data class Word (
    val id: String = "",
    val polish: String = "",
    val spanish: String = "",
    val url: String = ""
) : Serializable