package ru.naumov.androidstepper.topic

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.MaterialRepository

class TopicStoreFactory(
    private val storeFactory: StoreFactory,
    private val materialRepository: MaterialRepository,
    private val topicId: String
) {
    fun create(): TopicStore =
        object : TopicStore,
            Store<TopicIntent, TopicState, TopicLabel> by storeFactory.create<TopicIntent, TopicAction, TopicMessage, TopicState, TopicLabel>(
                name = "TopicStore",
                initialState = TopicState(),
                bootstrapper = coroutineBootstrapper {
                    dispatch(TopicAction.LoadMaterial)
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<TopicAction.LoadMaterial> { action ->
                        dispatch(TopicMessage.SetLoading(true))
                        launch {
                            val material = materialRepository.getMaterialByTopic(topicId)
                            dispatch(TopicMessage.SetContent(material?.content ?: ""))
                            dispatch(TopicMessage.SetLoading(false))
                        }
                    }
                    onIntent<TopicIntent.BackClicked> {
                        publish(TopicLabel.NavigateBack)
                    }
                    onIntent<TopicIntent.TestClicked> {
                        publish(TopicLabel.NavigateToTest)
                    }
                    onIntent<TopicIntent.ProgressChanged> { intent ->
                        dispatch(TopicMessage.SetProgress(intent.progress))
                        // Если нужен автокомплит при достижении 100%, здесь можно что-то еще добавить
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is TopicMessage.SetContent -> copy(content = msg.content)
                        is TopicMessage.SetImages -> copy(images = msg.images)
                        is TopicMessage.SetProgress -> copy(progress = msg.progress)
                        is TopicMessage.SetLoading -> copy(isLoading = msg.isLoading)
                        is TopicMessage.SetError -> copy(error = msg.error)
                    }
                }
            ) {}
}
