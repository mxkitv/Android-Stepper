package ru.naumov.androidstepper.courses

sealed interface CourseListIntent {
    // Нажатие на свой курс (переход к темам курса)
    data class MyCourseClicked(val courseId: String) : CourseListIntent

    // Нажатие на любой курс из “Все курсы” (переход к деталям курса)
    data class CourseClicked(val courseId: String) : CourseListIntent

    // Добавить курс в “мои курсы” (опционально, если нужен прямой add)
    data class AddCourse(val courseId: String) : CourseListIntent
}
