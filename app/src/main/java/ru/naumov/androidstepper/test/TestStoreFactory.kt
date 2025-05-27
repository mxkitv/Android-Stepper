package ru.naumov.androidstepper.test

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.TestRepository

class TestStoreFactory(
    private val storeFactory: StoreFactory,
    private val testRepository: TestRepository,
) {
    fun create(topicId: String): TestStore =
        object : TestStore,
            Store<TestIntent, TestState, TestLabel> by storeFactory.create<TestIntent, TestAction, TestMessage, TestState, TestLabel>(
                name = "TestStore",
                initialState = TestState(isLoading = true),
                bootstrapper = coroutineBootstrapper {
                    dispatch(TestAction.LoadTest)
                },
                executorFactory = coroutineExecutorFactory {
                    // Загрузка теста при старте
                    onAction<TestAction.LoadTest> {
                        launch {
                            testRepository.getTestByTopicId(topicId).collectLatest { entity ->
                                if (entity == null) {
                                    dispatch(TestMessage.Error("Тест не найден"))
                                } else {
                                    val questions = entity.questions
                                    dispatch(TestMessage.Loaded(questions))
                                }
                            }
                        }
                    }
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
                        is TestMessage.Loaded -> copy(
                            questions = msg.questions,
                            isLoading = false,
                            error = null,
                            showResult = false,
                            answers = emptyMap(),
                            correctCount = 0
                        )
                        is TestMessage.Error -> copy(
                            isLoading = false,
                            error = msg.message
                        )
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
