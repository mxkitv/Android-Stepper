package ru.naumov.androidstepper.courses

sealed interface CourseListMessage {
    data class SetMyCourses(val myCourses: List<CourseListItem>) : CourseListMessage
    data class SetAllCourses(val allCourses: List<CourseListItem>) : CourseListMessage
    data class SetLoading(val isLoading: Boolean) : CourseListMessage
    data class SetError(val error: String?) : CourseListMessage
}
