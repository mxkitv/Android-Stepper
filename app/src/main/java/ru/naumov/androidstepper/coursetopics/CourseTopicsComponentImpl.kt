// File: ru/naumov/androidstepper/coursetopics/CourseTopicsComponentImpl.kt
package ru.naumov.androidstepper.coursetopics

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
import ru.naumov.androidstepper.coursetopics.CourseTopicsComponent.Output.*
import ru.naumov.androidstepper.data.TopicRepository

class CourseTopicsComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val courseId: String,
    private val output: Consumer<CourseTopicsComponent.Output>
) : CourseTopicsComponent, ComponentContext by componentContext, KoinComponent {

    private val topicRepository: TopicRepository by inject()

    private val store =
        instanceKeeper.getStore {
            CourseTopicsStoreFactory(
                storeFactory = storeFactory,
                topicRepository = topicRepository
            ).create(courseId)
        }

    override val model: Value<CourseTopicsComponent.CourseTopicsModel> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    is CourseTopicsLabel.OpenTopic -> output.onNext(
                        OpenTopic(label.topicId)
                    )
                    is CourseTopicsLabel.NavigateBack -> output.onNext(Back)

                    is CourseTopicsLabel.ShowMessage -> TODO()
                }
            }
            .launchIn(scope)
    }

    override fun onBackClicked() {
        store.accept(CourseTopicsIntent.BackClicked)
    }

    override fun onTopicClicked(topicId: String) {
        store.accept(CourseTopicsIntent.TopicClicked(topicId))
    }
}
