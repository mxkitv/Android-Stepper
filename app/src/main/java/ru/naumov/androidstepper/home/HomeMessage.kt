package ru.naumov.androidstepper.home

sealed interface HomeMessage {
    data class SetUsername(val username: String) : HomeMessage
    data class SetCourses(val courses: List<HomeCourse>) : HomeMessage
    data class SetRecent(val recent: List<RecentEvent>) : HomeMessage
    data class SetProgress(val progress: Float) : HomeMessage
    data class SetLoading(val isLoading: Boolean) : HomeMessage
    data class SetError(val error: String?) : HomeMessage
}
