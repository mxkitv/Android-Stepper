package ru.naumov.androidstepper.coursedetail

import com.arkivanov.decompose.value.Value

interface CourseDetailComponent {
    val model: Value<CourseDetailModel>

    fun onBackClicked()
    fun onAddCourseClicked()

    data class CourseDetailModel(
        val course: CourseDetailItem? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Output {
        object Back : Output
        object CourseAdded : Output
    }
}
