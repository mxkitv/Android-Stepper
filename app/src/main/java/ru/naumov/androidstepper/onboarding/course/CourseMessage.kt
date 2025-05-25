package ru.naumov.androidstepper.onboarding.course

sealed interface CourseMessage {
    data class CourseSelected(val courseId: String) : CourseMessage
    data class CourseDeselected(val courseId: String) : CourseMessage
    data class SetLoading(val value: Boolean) : CourseMessage
    data class SetError(val value: String?) : CourseMessage
}
