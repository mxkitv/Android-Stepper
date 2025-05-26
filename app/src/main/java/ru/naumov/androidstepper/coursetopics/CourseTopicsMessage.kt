// File: ru/naumov/androidstepper/coursetopics/CourseTopicsMessage.kt
package ru.naumov.androidstepper.coursetopics

import ru.naumov.androidstepper.data.database.TopicEntity

sealed interface CourseTopicsMessage {
    data class SetCourseTitle(val title: String) : CourseTopicsMessage
    data class SetTopics(val topics: List<TopicEntity>) : CourseTopicsMessage
    data class SetProgress(val progress: Float) : CourseTopicsMessage
    data class SetLoading(val isLoading: Boolean) : CourseTopicsMessage
    data class SetError(val error: String?) : CourseTopicsMessage
}
