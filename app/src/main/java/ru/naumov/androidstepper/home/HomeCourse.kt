package ru.naumov.androidstepper.home

data class HomeCourse(
    val id: String,
    val title: String,
    val progress: Float, // от 0.0 до 1.0
    val iconRes: Int? = null // можно null, если иконка не нужна
)