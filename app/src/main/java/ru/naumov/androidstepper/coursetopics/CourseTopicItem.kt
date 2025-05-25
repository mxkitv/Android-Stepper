// File: ru/naumov/androidstepper/coursetopics/CourseTopicItem.kt
package ru.naumov.androidstepper.coursetopics

data class CourseTopicItem(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false, // true если тема пройдена
    val progress: Float? = null       // от 0.0 до 1.0 если есть частичный прогресс, иначе null
)
