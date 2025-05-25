package ru.naumov.androidstepper.topic

import com.arkivanov.decompose.value.Value

interface TopicComponent {
    val model: Value<TopicModel>

    fun onBackClicked()
    fun onTestClicked()
    fun onProgressChanged(progress: Float)

    data class TopicModel(
        val content: String = "",
        val images: List<String> = emptyList(),
        val progress: Float = 0f,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Output {
        object NavigateBack : Output
        object NavigateToTest : Output
    }
}
