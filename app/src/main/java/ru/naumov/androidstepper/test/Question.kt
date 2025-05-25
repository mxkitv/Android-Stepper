package ru.naumov.androidstepper.test

data class Question(
    val id: String,
    val text: String,
    val answers: List<String>,
    val correctAnswer: Int
)