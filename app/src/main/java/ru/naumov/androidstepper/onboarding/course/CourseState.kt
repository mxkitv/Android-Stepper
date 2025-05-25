package ru.naumov.androidstepper.onboarding.course

import ru.naumov.androidstepper.data.database.CourseEntity

data class CourseState(
    val courses: List<CourseEntity> = emptyList(),
    val selectedCourseIds: Set<String> = emptySet(),
    val isLoading: Boolean = false
)
