package ru.naumov.androidstepper.home

data class HomeState(
    val username: String = "",
    val courses: List<HomeCourse> = emptyList(),
    val recentEvents: List<RecentEvent> = emptyList(),
    val progress: Float = 0f, // общий прогресс (0..1)
    val isLoading: Boolean = false,
    val error: String? = null
)
