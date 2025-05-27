package ru.naumov.androidstepper.coursedetail

import com.arkivanov.decompose.value.Value
import ru.naumov.androidstepper.data.database.CourseEntity

interface CourseDetailComponent {
    val model: Value<CourseDetailModel>

    fun onBackClicked()
    fun onAddCourseClicked()

    data class CourseDetailModel(
        val course: CourseEntity? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Output {
        object Back : Output
        object CourseAdded : Output
    }
}
