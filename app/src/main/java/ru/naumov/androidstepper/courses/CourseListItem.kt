package ru.naumov.androidstepper.courses

data class CourseListItem(
    val id: String,
    val title: String,
    val description: String,
    val topicsCount: Int,
    val progress: Float = 0f, // Для "моих курсов" (0.0 - 1.0), для остальных всегда 0f
    val iconRes: Int? = null // По желанию, для иконки курса
)
