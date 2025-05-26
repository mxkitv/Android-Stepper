// File: ru/naumov/androidstepper/topic/TopicComponentImpl.kt
package ru.naumov.androidstepper.topic

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
import ru.naumov.androidstepper.data.MaterialRepository

class TopicComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    topicId: String,
    private val output: Consumer<TopicComponent.Output>
) : TopicComponent, ComponentContext by componentContext, KoinComponent {

    private val materialRepository: MaterialRepository by inject()

    private val store =
        instanceKeeper.getStore {
            TopicStoreFactory(
                storeFactory = storeFactory,
                materialRepository = materialRepository,
                topicId = topicId
            ).create()
        }

    override val model: Value<TopicComponent.TopicModel> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    TopicLabel.NavigateBack -> output.onNext(TopicComponent.Output.NavigateBack)
                    TopicLabel.NavigateToTest -> output.onNext(TopicComponent.Output.NavigateToTest)
                    is TopicLabel.ShowMessage -> { /* Можно показать Snackbar или Toast */ }
                }
            }
            .launchIn(scope)
    }

    override fun onBackClicked() {
        store.accept(TopicIntent.BackClicked)
    }

    override fun onTestClicked() {
        store.accept(TopicIntent.TestClicked)
    }

    override fun onProgressChanged(progress: Float) {
        store.accept(TopicIntent.ProgressChanged(progress))
    }
}