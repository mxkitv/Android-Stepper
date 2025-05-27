package ru.naumov.androidstepper.coursedetail

import ru.naumov.androidstepper.coursedetail.CourseDetailItem
import ru.naumov.androidstepper.data.database.CourseEntity

sealed interface CourseDetailMessage {
    data class SetCourse(val course: CourseEntity) : CourseDetailMessage
    data class SetLoading(val isLoading: Boolean) : CourseDetailMessage
    data class SetError(val error: String?) : CourseDetailMessage
}
