package ru.naumov.androidstepper.onboarding.course

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.CourseRepository
import ru.naumov.androidstepper.data.SelectedCourseRepository

class CourseStoreFactory(
    private val storeFactory: StoreFactory,
    private val selectedCourseRepository: SelectedCourseRepository,
    private val courseRepository: CourseRepository,
    // Можно прокидывать список доступных курсов при необходимости
) {
    fun create(): CourseStore =
        object : CourseStore,
            Store<CourseIntent, CourseState, CourseLabel> by storeFactory.create<CourseIntent, CourseAction, CourseMessage, CourseState, CourseLabel>(
                name = "CourseStore",
                initialState = CourseState(),
                bootstrapper = coroutineBootstrapper {
                    dispatch(CourseAction.LoadCourses)
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<CourseAction.LoadCourses> {
                        dispatch(CourseMessage.SetLoading(true))
                        launch {
                            val allCourses = courseRepository.getAllCourses()
                            val selectedIds = selectedCourseRepository.getSelectedCourses()
                            dispatch(CourseMessage.SetCourses(allCourses, selectedIds))
                        }
                    }
                    onIntent<CourseIntent.ToggleCourse> { intent ->
                        val currentSelected = state().selectedCourseIds.toMutableSet()
                        if (currentSelected.contains(intent.courseId))
                            currentSelected.remove(intent.courseId)
                        else
                            currentSelected.add(intent.courseId)
                        dispatch(CourseMessage.SetSelectedCourses(currentSelected))
                    }
                    onIntent<CourseIntent.ContinueClicked> {
                        launch {
                            selectedCourseRepository.setSelectedCourses(state().selectedCourseIds.toList())
                            publish(CourseLabel.NavigateNext)
                        }
                    }
                    onIntent<CourseIntent.BackClicked> {
                        publish(CourseLabel.NavigateBack)
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is CourseMessage.SetCourses -> copy(
                            courses = msg.courses,
                            selectedCourseIds = msg.selectedIds.toSet(),
                            isLoading = false
                        )
                        is CourseMessage.SetSelectedCourses -> copy(selectedCourseIds = msg.selected)
                        is CourseMessage.SetLoading -> copy(isLoading = msg.value)
                    }
                }
            ) {}
}

