package ru.naumov.androidstepper.onboarding.course

import com.arkivanov.decompose.value.Value

interface CourseComponent {
    val model: Value<CourseModel>

    fun onCourseSelected(courseId: String)
    fun onCourseDeselected(courseId: String)
    fun onContinue()
    fun onBack()

    data class CourseModel(
        val selectedCourseIds: Set<String> = emptySet(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface CourseOutput {
        object NavigateNext : CourseOutput
        object NavigateBack : CourseOutput
    }
}
