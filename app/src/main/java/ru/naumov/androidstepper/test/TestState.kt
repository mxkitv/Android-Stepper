package ru.naumov.androidstepper.test

data class TestState(
    val questions: List<Question> = emptyList(),
    val answers: Map<String, Int> = emptyMap(), // questionId -> answerIndex
    val isLoading: Boolean = false,
    val error: String? = null,
    val showResult: Boolean = false,
    val correctCount: Int = 0,
)
