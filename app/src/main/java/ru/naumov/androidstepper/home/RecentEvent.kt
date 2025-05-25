package ru.naumov.androidstepper.home

data class RecentEvent(
    val id: String,
    val title: String,
    val courseId: String,
    val courseTitle: String,
    val iconRes: Int? = null,
    val timestamp: Long // unix-время или другой идентификатор времени
)
