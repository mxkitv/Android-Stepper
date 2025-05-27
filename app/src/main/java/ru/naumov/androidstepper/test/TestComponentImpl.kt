package ru.naumov.androidstepper.test

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.badoo.reaktive.base.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.naumov.androidstepper.asValue
import ru.naumov.androidstepper.data.TestRepository

class TestComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val topicId: String,
    private val output: Consumer<TestComponent.Output>
) : TestComponent, ComponentContext by componentContext, KoinComponent {

    private val testRepository: TestRepository by inject()

    private val store =
        instanceKeeper.getStore {
            TestStoreFactory(
                storeFactory = storeFactory,
                testRepository = testRepository
            ).create(topicId = topicId)
        }

    override val model: Value<TestComponent.TestModel> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    TestLabel.NavigateBack -> output.onNext(TestComponent.Output.NavigateBack)
                    TestLabel.ShowResult -> output.onNext(TestComponent.Output.ShowResult)
                    is TestLabel.ShowMessage -> { /* В будущем: показывать Snackbar, Toast и т.д. */ }
                }
            }
            .launchIn(scope)
    }

    override fun onAnswerSelected(questionId: String, answerIndex: Int) {
        store.accept(TestIntent.AnswerSelected(questionId, answerIndex))
    }

    override fun onSubmit() {
        store.accept(TestIntent.SubmitClicked)
    }

    override fun onRetry() {
        store.accept(TestIntent.RetryClicked)
    }

    override fun onBack() {
        store.accept(TestIntent.BackClicked)
    }

    override fun onContinue() {
        output.onNext(TestComponent.Output.Continue)
    }
}
