package ru.naumov.androidstepper.onboarding.course

import com.arkivanov.decompose.value.Value
import ru.naumov.androidstepper.data.database.CourseEntity

interface CourseComponent {
    val model: Value<CourseModel>

    fun onCourseToggled(courseId: String)
    fun onContinue()
    fun onBack()

    data class CourseModel(
        val courses: List<CourseEntity> = emptyList(),
        val selectedCourseIds: Set<String> = emptySet(),
        val isLoading: Boolean = false
    )

    sealed interface CourseOutput {
        object NavigateNext : CourseOutput
        object NavigateBack : CourseOutput
    }
}
