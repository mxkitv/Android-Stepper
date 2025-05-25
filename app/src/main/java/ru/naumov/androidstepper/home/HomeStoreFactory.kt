package ru.naumov.androidstepper.home

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class HomeStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): HomeStore =
        object : HomeStore,
            Store<HomeIntent, HomeState, HomeLabel> by storeFactory.create<HomeIntent, Nothing, HomeMessage, HomeState, HomeLabel>(
                name = "HomeStore",
                initialState = HomeState(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<HomeIntent.CourseClicked> { intent ->
                        publish(HomeLabel.OpenCourse(intent.courseId))
                    }
                    onIntent<HomeIntent.RecentClicked> { intent ->
                        publish(HomeLabel.OpenRecent(intent.courseId, intent.topicId))
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is HomeMessage.SetUsername -> copy(username = msg.username)
                        is HomeMessage.SetCourses -> copy(courses = msg.courses)
                        is HomeMessage.SetRecent -> copy(recentEvents = msg.recent)
                        is HomeMessage.SetProgress -> copy(progress = msg.progress)
                        is HomeMessage.SetLoading -> copy(isLoading = msg.isLoading)
                        is HomeMessage.SetError -> copy(error = msg.error)
                    }
                }
            ) {}
}
