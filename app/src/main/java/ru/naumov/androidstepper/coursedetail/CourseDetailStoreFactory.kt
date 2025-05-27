package ru.naumov.androidstepper.coursedetail

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.CourseRepository
import ru.naumov.androidstepper.data.SelectedCourseRepository

class CourseDetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val courseRepository: CourseRepository,
    private val selectedCourseRepository: SelectedCourseRepository
) {
    fun create(courseId: String): CourseDetailStore =
        object : CourseDetailStore,
            Store<CourseDetailIntent, CourseDetailState, CourseDetailLabel> by storeFactory.create<CourseDetailIntent, CourseDetailAction, CourseDetailMessage, CourseDetailState, CourseDetailLabel>(
                name = "CourseDetailStore",
                initialState = CourseDetailState(),
                bootstrapper = coroutineBootstrapper {
                    dispatch(CourseDetailAction.LoadCourse)
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<CourseDetailAction.LoadCourse> { action ->
                        dispatch(CourseDetailMessage.SetLoading(true))
                        launch {
                            courseRepository.getCoursesByIds(listOf(courseId)).collect { courses ->
                                dispatch(CourseDetailMessage.SetCourse(courses.first()))
                            }
                        }
                    }
                    onIntent<CourseDetailIntent.BackClicked> {
                        // Обычно переход обрабатывается во ViewModel/Component, а не через Label
                        // Можно здесь publish label, если потребуется
                    }
                    onIntent<CourseDetailIntent.AddCourseClicked> {
                        dispatch(CourseDetailMessage.SetLoading(true))
                        launch {
                            selectedCourseRepository.selectCourse(courseId)
                            dispatch(CourseDetailMessage.SetLoading(false))
                        }
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is CourseDetailMessage.SetCourse -> copy(course = msg.course, isLoading = false)
                        is CourseDetailMessage.SetLoading -> copy(isLoading = msg.isLoading)
                        is CourseDetailMessage.SetError -> copy(error = msg.error)
                    }
                }
            ) {}
}
