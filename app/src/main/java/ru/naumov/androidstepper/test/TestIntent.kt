package ru.naumov.androidstepper.test

sealed interface TestIntent {
    object BackClicked : TestIntent
    data class AnswerSelected(val questionId: String, val answerIndex: Int) : TestIntent
    object SubmitClicked : TestIntent
    object RetryClicked : TestIntent
}
