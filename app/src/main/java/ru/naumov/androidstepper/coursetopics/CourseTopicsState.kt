// File: ru/naumov/androidstepper/coursetopics/CourseTopicsState.kt
package ru.naumov.androidstepper.coursetopics

import ru.naumov.androidstepper.data.database.TopicEntity

data class CourseTopicsState(
    val courseId: String = "",
    val courseTitle: String = "",
    val topics: List<TopicEntity> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
