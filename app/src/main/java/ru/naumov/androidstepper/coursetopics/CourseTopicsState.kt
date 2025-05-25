// File: ru/naumov/androidstepper/coursetopics/CourseTopicsState.kt
package ru.naumov.androidstepper.coursetopics

data class CourseTopicsState(
    val courseId: String = "",
    val courseTitle: String = "",
    val topics: List<CourseTopicItem> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
