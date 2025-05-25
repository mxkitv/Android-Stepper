package ru.naumov.androidstepper.topic

sealed interface TopicLabel {
    object NavigateBack : TopicLabel
    object NavigateToTest : TopicLabel
    data class ShowMessage(val message: String) : TopicLabel // для будущих ошибок/уведомлений
}
