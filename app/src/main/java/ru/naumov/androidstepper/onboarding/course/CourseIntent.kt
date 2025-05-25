package ru.naumov.androidstepper.onboarding.course

sealed interface CourseIntent {
    object LoadCourses : CourseIntent
    data class ToggleCourse(val courseId: String) : CourseIntent
    object ContinueClicked : CourseIntent
    object BackClicked : CourseIntent
}

