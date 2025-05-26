// File: ru/naumov/androidstepper/coursetopics/CourseTopicsComponent.kt
package ru.naumov.androidstepper.coursetopics

import com.arkivanov.decompose.value.Value
import ru.naumov.androidstepper.data.database.TopicEntity

interface CourseTopicsComponent {
    val model: Value<CourseTopicsModel>

    fun onTopicClicked(topicId: String)
    fun onBackClicked()

    data class CourseTopicsModel(
        val courseId: String = "",
        val courseTitle: String = "",
        val topics: List<TopicEntity> = emptyList(),
        val completedCount: Int = 0,
        val totalCount: Int = 0,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Output {
        data class OpenTopic(val topicId: String) : Output
        object Back : Output
    }
}
