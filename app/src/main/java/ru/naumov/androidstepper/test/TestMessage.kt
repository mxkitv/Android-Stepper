package ru.naumov.androidstepper.test

import ru.naumov.androidstepper.data.database.Question

sealed interface TestMessage {
    data class Loaded(val questions: List<Question>) : TestMessage
    data class Error(val message: String) : TestMessage
    data class AnswerChosen(val questionId: String, val answerIndex: Int) : TestMessage
    object Submitted : TestMessage
    object Retried : TestMessage
}
