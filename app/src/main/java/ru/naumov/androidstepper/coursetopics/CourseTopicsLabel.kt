// File: ru/naumov/androidstepper/coursetopics/CourseTopicsLabel.kt
package ru.naumov.androidstepper.coursetopics

sealed interface CourseTopicsLabel {
    // Навигация к теме для изучения
    data class OpenTopic(val topicId: String) : CourseTopicsLabel

    // Навигация назад
    object NavigateBack : CourseTopicsLabel

    // Можно добавить ShowMessage, если потребуется отображать ошибки/инфо
    data class ShowMessage(val message: String) : CourseTopicsLabel
}
