package ru.naumov.androidstepper.courses

sealed interface CourseListLabel {
    // Навигация к темам своего курса
    data class OpenMyCourse(val courseId: String) : CourseListLabel

    // Навигация к деталям любого курса
    data class OpenCourseDetail(val courseId: String) : CourseListLabel

    // Уведомление об ошибке/успехе (опционально)
    data class ShowMessage(val message: String) : CourseListLabel
}
