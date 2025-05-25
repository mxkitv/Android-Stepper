package ru.naumov.androidstepper.home

import com.arkivanov.decompose.value.Value

interface HomeComponent {
    val model: Value<HomeModel>

    fun onCourseClicked(courseId: String)
    fun onRecentClicked(courseId: String, topicId: String)

    data class HomeModel(
        val username: String = "",
        val courses: List<HomeCourse> = emptyList(),
        val recentEvents: List<RecentEvent> = emptyList(),
        val progress: Float = 0f,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Output {
        data class OpenCourse(val courseId: String) : Output
        data class OpenRecent(val courseId: String, val topicId: String) : Output
    }
}
