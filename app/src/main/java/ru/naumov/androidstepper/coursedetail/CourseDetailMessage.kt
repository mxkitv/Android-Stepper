package ru.naumov.androidstepper.coursedetail

import ru.naumov.androidstepper.coursedetail.CourseDetailItem

sealed interface CourseDetailMessage {
    data class SetCourse(val course: CourseDetailItem) : CourseDetailMessage
    data class SetLoading(val isLoading: Boolean) : CourseDetailMessage
    data class SetError(val error: String?) : CourseDetailMessage
}
