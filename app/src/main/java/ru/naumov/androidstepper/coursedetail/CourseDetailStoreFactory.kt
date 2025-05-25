package ru.naumov.androidstepper.coursedetail

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class CourseDetailStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): CourseDetailStore =
        object : CourseDetailStore,
            Store<CourseDetailIntent, CourseDetailState, CourseDetailLabel> by storeFactory.create<CourseDetailIntent, Nothing, CourseDetailMessage, CourseDetailState, CourseDetailLabel>(
                name = "CourseDetailStore",
                initialState = CourseDetailState(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<CourseDetailIntent.BackClicked> {
                        // Обычно переход обрабатывается во ViewModel/Component, а не через Label
                        // Можно здесь publish label, если потребуется
                    }
                    onIntent<CourseDetailIntent.AddCourseClicked> {
                        // Можно обработать добавление курса (например, publish label/side effect)
                        publish(CourseDetailLabel.ShowMessage("Курс добавлен!"))
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is CourseDetailMessage.SetCourse -> copy(course = msg.course)
                        is CourseDetailMessage.SetLoading -> copy(isLoading = msg.isLoading)
                        is CourseDetailMessage.SetError -> copy(error = msg.error)
                    }
                }
            ) {}
}
