package ru.naumov.androidstepper.courses

data class CourseListState(
    val myCourses: List<CourseListItem> = emptyList(),
    val allCourses: List<CourseListItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
