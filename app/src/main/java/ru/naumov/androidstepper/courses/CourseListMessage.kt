package ru.naumov.androidstepper.courses

import ru.naumov.androidstepper.data.database.CourseEntity

sealed interface CourseListMessage {
    data class SetMyCourses(val myCourses: List<CourseEntity>) : CourseListMessage
    data class SetAllCourses(val allCourses: List<CourseEntity>) : CourseListMessage
    data class SetLoading(val isLoading: Boolean) : CourseListMessage
    data class SetError(val error: String?) : CourseListMessage
}
