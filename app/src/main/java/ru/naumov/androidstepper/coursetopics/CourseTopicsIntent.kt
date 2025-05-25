package ru.naumov.androidstepper.coursetopics

sealed interface CourseTopicsIntent {
    // Нажатие на тему (переход к материалу темы)
    data class TopicClicked(val topicId: String) : CourseTopicsIntent

    // Клик по "Назад"
    object BackClicked : CourseTopicsIntent
}
