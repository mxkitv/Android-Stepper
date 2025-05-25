package ru.naumov.androidstepper.home

sealed interface HomeIntent {
    data class CourseClicked(val courseId: String) : HomeIntent
    data class RecentClicked(val courseId: String, val topicId: String) : HomeIntent
}
