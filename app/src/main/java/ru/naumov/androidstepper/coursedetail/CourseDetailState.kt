package ru.naumov.androidstepper.coursedetail

data class CourseDetailState(
    val course: CourseDetailItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
