// File: ru/naumov/androidstepper/coursetopics/CourseTopicsStoreFactory.kt
package ru.naumov.androidstepper.coursetopics

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class CourseTopicsStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(courseId: String): CourseTopicsStore =
        object : CourseTopicsStore,
            Store<CourseTopicsIntent, CourseTopicsState, CourseTopicsLabel> by storeFactory.create<CourseTopicsIntent, Nothing, CourseTopicsMessage, CourseTopicsState, CourseTopicsLabel>(
                name = "CourseTopicsStore",
                initialState = CourseTopicsState(courseId = courseId),
                executorFactory = coroutineExecutorFactory {
                    onIntent<CourseTopicsIntent.BackClicked> {
                        publish(CourseTopicsLabel.NavigateBack)
                    }
                    onIntent<CourseTopicsIntent.TopicClicked> { intent ->
                        publish(CourseTopicsLabel.OpenTopic(intent.topicId))
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is CourseTopicsMessage.SetCourseTitle -> copy(courseTitle = msg.title)
                        is CourseTopicsMessage.SetTopics -> copy(topics = msg.topics)
                        is CourseTopicsMessage.SetLoading -> copy(isLoading = msg.isLoading)
                        is CourseTopicsMessage.SetError -> copy(error = msg.error)
                        is CourseTopicsMessage.SetProgress -> TODO()
                    }
                }
            ) {}
}
