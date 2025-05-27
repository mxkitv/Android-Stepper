// File: ru/naumov/androidstepper/coursetopics/CourseTopicsStoreFactory.kt
package ru.naumov.androidstepper.coursetopics

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.CourseRepository
import ru.naumov.androidstepper.data.TopicRepository

class CourseTopicsStoreFactory(
    private val storeFactory: StoreFactory,
    private val topicRepository: TopicRepository,
    private val courseRepository: CourseRepository
) {
    fun create(courseId: String): CourseTopicsStore =
        object : CourseTopicsStore,
            Store<CourseTopicsIntent, CourseTopicsState, CourseTopicsLabel> by storeFactory.create<CourseTopicsIntent, CourseTopicsAction, CourseTopicsMessage, CourseTopicsState, CourseTopicsLabel>(
                name = "CourseTopicsStore",
                initialState = CourseTopicsState(courseId = courseId),
                bootstrapper = coroutineBootstrapper {
                    dispatch(CourseTopicsAction.LoadTopics)
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<CourseTopicsAction.LoadTopics> {
                        dispatch(CourseTopicsMessage.SetLoading(true))
                        launch {
                            val topics = topicRepository.getTopicsByCourse(courseId)
                            courseRepository.getCoursesByIds(listOf(courseId)).collect { courseTitle ->
                                dispatch(CourseTopicsMessage.SetCourseTitle(courseTitle.first().title))
                                dispatch(CourseTopicsMessage.SetTopics(topics))
                                dispatch(CourseTopicsMessage.SetLoading(false))
                            }
                        }
                    }
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
