package ru.naumov.androidstepper.courses

import ru.naumov.androidstepper.data.database.CourseEntity

data class CourseListState(
    val myCourses: List<CourseEntity> = emptyList(),
    val allCourses: List<CourseEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
