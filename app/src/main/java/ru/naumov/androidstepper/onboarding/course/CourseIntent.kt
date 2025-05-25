package ru.naumov.androidstepper.onboarding.course

sealed interface CourseIntent {
    data class CourseSelected(val courseId: String) : CourseIntent
    data class CourseDeselected(val courseId: String) : CourseIntent
    object ContinueClicked : CourseIntent
    object BackClicked : CourseIntent
}
