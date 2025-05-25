package ru.naumov.androidstepper.topic

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class TopicStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): TopicStore =
        object : TopicStore,
            Store<TopicIntent, TopicState, TopicLabel> by storeFactory.create<TopicIntent, Nothing, TopicMessage, TopicState, TopicLabel>(
                name = "TopicStore",
                initialState = TopicState(),
                executorFactory = coroutineExecutorFactory {
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
