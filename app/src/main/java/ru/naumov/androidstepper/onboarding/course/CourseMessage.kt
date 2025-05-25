package ru.naumov.androidstepper.onboarding.course

import ru.naumov.androidstepper.data.database.CourseEntity

sealed interface CourseMessage {
    data class SetCourses(val courses: List<CourseEntity>, val selectedIds: List<String>) : CourseMessage
    data class SetSelectedCourses(val selected: Set<String>) : CourseMessage
    data class SetLoading(val value: Boolean) : CourseMessage
}
