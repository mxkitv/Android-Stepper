package ru.naumov.androidstepper.test

sealed interface TestMessage {
    data class AnswerChosen(val questionId: String, val answerIndex: Int) : TestMessage
    object Submitted : TestMessage
    object Retried : TestMessage
}
