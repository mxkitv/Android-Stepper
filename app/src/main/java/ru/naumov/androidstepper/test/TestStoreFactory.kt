package ru.naumov.androidstepper.test

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class TestStoreFactory(
    private val storeFactory: StoreFactory,
) {
    fun create(): TestStore =
        object : TestStore,
            Store<TestIntent, TestState, TestLabel> by storeFactory.create<TestIntent, Nothing, TestMessage, TestState, TestLabel>(
                name = "TestStore",
                initialState = TestState(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<TestIntent.BackClicked> {
                        publish(TestLabel.NavigateBack)
                    }
                    onIntent<TestIntent.AnswerSelected> { intent ->
                        dispatch(TestMessage.AnswerChosen(intent.questionId, intent.answerIndex))
                    }
                    onIntent<TestIntent.SubmitClicked> {
                        dispatch(TestMessage.Submitted)
                        publish(TestLabel.ShowResult)
                    }
                    onIntent<TestIntent.RetryClicked> {
                        dispatch(TestMessage.Retried)
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is TestMessage.AnswerChosen -> copy(
                            answers = answers + (msg.questionId to msg.answerIndex),
                            error = null
                        )
                        is TestMessage.Submitted -> {
                            val correctCount = questions.count { q ->
                                answers[q.id] == q.correctAnswer
                            }
                            copy(
                                showResult = true,
                                correctCount = correctCount
                            )
                        }
                        is TestMessage.Retried -> copy(
                            answers = emptyMap(),
                            showResult = false,
                            correctCount = 0,
                            error = null
                        )
                    }
                }
            ) {}
}
