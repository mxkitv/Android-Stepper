package ru.naumov.androidstepper.home

sealed interface HomeLabel {
    data class OpenCourse(val courseId: String) : HomeLabel
    data class OpenRecent(val courseId: String, val topicId: String) : HomeLabel
}