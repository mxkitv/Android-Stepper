package ru.naumov.androidstepper.onboarding.course

data class CourseState(
    val selectedCourseIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)
