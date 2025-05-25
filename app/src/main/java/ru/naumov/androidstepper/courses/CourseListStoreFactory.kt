package ru.naumov.androidstepper.courses

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class CourseListStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): CourseListStore =
        object : CourseListStore,
            Store<CourseListIntent, CourseListState, CourseListLabel> by storeFactory.create<CourseListIntent, Nothing, CourseListMessage, CourseListState, CourseListLabel>(
                name = "CourseListStore",
                initialState = CourseListState(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<CourseListIntent.MyCourseClicked> { intent ->
                        publish(CourseListLabel.OpenMyCourse(intent.courseId))
                    }
                    onIntent<CourseListIntent.CourseClicked> { intent ->
                        publish(CourseListLabel.OpenCourseDetail(intent.courseId))
                    }
                    onIntent<CourseListIntent.AddCourse> { intent ->
                        // Просто для примера: можно обработать добавление в мои курсы и обновить список
                        // publish(CourseListLabel.ShowMessage("Курс добавлен")) // опционально
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is CourseListMessage.SetMyCourses -> copy(myCourses = msg.myCourses)
                        is CourseListMessage.SetAllCourses -> copy(allCourses = msg.allCourses)
                        is CourseListMessage.SetLoading -> copy(isLoading = msg.isLoading)
                        is CourseListMessage.SetError -> copy(error = msg.error)
                    }
                }
            ) {}
}
