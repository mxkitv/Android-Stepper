package ru.naumov.androidstepper.topic

sealed interface TopicMessage {
    data class SetContent(val content: String) : TopicMessage
    data class SetImages(val images: List<String>) : TopicMessage // Пути/ссылки на изображения, если есть
    data class SetProgress(val progress: Float) : TopicMessage
    data class SetLoading(val isLoading: Boolean) : TopicMessage
    data class SetError(val error: String?) : TopicMessage
}
