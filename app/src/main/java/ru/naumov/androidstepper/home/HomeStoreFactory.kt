package ru.naumov.androidstepper.home

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.CourseRepository
import ru.naumov.androidstepper.data.SelectedCourseRepository

class HomeStoreFactory(
    private val storeFactory: StoreFactory,
    private val selectedCourseRepository: SelectedCourseRepository,
    private val courseRepository: CourseRepository
) {
    fun create(): HomeStore =
        object : HomeStore,
            Store<HomeIntent, HomeState, HomeLabel> by storeFactory.create<HomeIntent, HomeAction, HomeMessage, HomeState, HomeLabel>(
                name = "HomeStore",
                initialState = HomeState(),
                bootstrapper = coroutineBootstrapper {
                    dispatch(HomeAction.LoadMyCourses)
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<HomeAction.LoadMyCourses> { action ->
                        dispatch(HomeMessage.SetLoading(true))
                        launch {
                            val ids = selectedCourseRepository.getSelectedCourses()
                            val courses = courseRepository.getCoursesByIds(ids)
                            dispatch(HomeMessage.SetCourses(courses))
                        }
                    }
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
                        is HomeMessage.SetCourses -> copy(courses = msg.courses, isLoading = false)
                        is HomeMessage.SetRecent -> copy(recentEvents = msg.recent)
                        is HomeMessage.SetProgress -> copy(progress = msg.progress)
                        is HomeMessage.SetLoading -> copy(isLoading = msg.isLoading)
                        is HomeMessage.SetError -> copy(error = msg.error)
                    }
                }
            ) {}
}
