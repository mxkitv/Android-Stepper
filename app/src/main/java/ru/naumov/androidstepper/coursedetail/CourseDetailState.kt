package ru.naumov.androidstepper.coursedetail

import ru.naumov.androidstepper.data.database.CourseEntity

data class CourseDetailState(
    val course: CourseEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
