package ru.naumov.androidstepper.test

import com.arkivanov.decompose.value.Value

interface TestComponent {
    val model: Value<TestModel>

    fun onAnswerSelected(questionId: String, answerIndex: Int)
    fun onSubmit()
    fun onRetry()
    fun onBack()

    data class TestModel(
        val questions: List<Question> = emptyList(),
        val answers: Map<String, Int> = emptyMap(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val showResult: Boolean = false,
        val correctCount: Int = 0,
    )

    sealed interface Output {
        object NavigateBack : Output
        object ShowResult : Output
    }
}
