package ru.naumov.androidstepper.courses

import com.arkivanov.decompose.value.Value
import ru.naumov.androidstepper.data.database.CourseEntity

interface CourseListComponent {
    val model: Value<Model>

    fun onMyCourseClicked(courseId: String)
    fun onCourseClicked(courseId: String)
    fun onAddCourse(courseId: String)

    data class Model(
        val myCourses: List<CourseEntity> = emptyList(),
        val allCourses: List<CourseEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Output {
        data class OpenMyCourse(val courseId: String) : Output
        data class OpenCourseDetail(val courseId: String) : Output
        data class ShowMessage(val message: String) : Output
    }
}
