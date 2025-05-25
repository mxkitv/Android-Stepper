package ru.naumov.androidstepper.onboarding.course

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class CourseStoreFactory(
    private val storeFactory: StoreFactory,
    // Можно прокидывать список доступных курсов при необходимости
) {
    fun create(): CourseStore =
        object : CourseStore,
            Store<CourseIntent, CourseState, CourseLabel> by storeFactory.create<CourseIntent, Nothing, CourseMessage, CourseState, CourseLabel>(
                name = "CourseStore",
                initialState = CourseState(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<CourseIntent.CourseSelected> { intent ->
                        dispatch(CourseMessage.CourseSelected(intent.courseId))
                        dispatch(CourseMessage.SetError(null))
                    }
                    onIntent<CourseIntent.CourseDeselected> { intent ->
                        dispatch(CourseMessage.CourseDeselected(intent.courseId))
                        dispatch(CourseMessage.SetError(null))
                    }
                    onIntent<CourseIntent.ContinueClicked> {
                        val selected = state().selectedCourseIds
                        if (selected.isEmpty()) {
                            dispatch(CourseMessage.SetError("course_error_choose"))
                        } else {
                            dispatch(CourseMessage.SetLoading(true))
                            // Здесь можно эмулировать сохранение, переход и т.д.
                            publish(CourseLabel.NavigateNext)
                            dispatch(CourseMessage.SetLoading(false))
                        }
                    }
                    onIntent<CourseIntent.BackClicked> {
                        publish(CourseLabel.NavigateBack)
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is CourseMessage.CourseSelected -> copy(
                            selectedCourseIds = selectedCourseIds + msg.courseId
                        )
                        is CourseMessage.CourseDeselected -> copy(
                            selectedCourseIds = selectedCourseIds - msg.courseId
                        )
                        is CourseMessage.SetLoading -> copy(
                            isLoading = msg.value
                        )
                        is CourseMessage.SetError -> copy(
                            error = msg.value
                        )
                    }
                }
            ) {}
}

