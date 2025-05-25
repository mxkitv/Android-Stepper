package ru.naumov.androidstepper.topic

data class TopicState(
    val content: String = "",
    val images: List<String> = emptyList(),
    val progress: Float = 0f, // 0.0..1.0, сколько пользователь проскроллил
    val isLoading: Boolean = false,
    val error: String? = null
)
