package ru.naumov.androidstepper.courses

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.CourseRepository
import ru.naumov.androidstepper.data.SelectedCourseRepository

class CourseListStoreFactory(
    private val storeFactory: StoreFactory,
    private val selectedCourseRepository: SelectedCourseRepository,
    private val courseRepository: CourseRepository
) {
    fun create(): CourseListStore =
        object : CourseListStore,
            Store<CourseListIntent, CourseListState, CourseListLabel> by storeFactory.create<CourseListIntent, CourseListAction, CourseListMessage, CourseListState, CourseListLabel>(
                name = "CourseListStore",
                initialState = CourseListState(),
                bootstrapper = coroutineBootstrapper {
                    dispatch(CourseListAction.LoadCourses)
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<CourseListAction.LoadCourses> { action ->
                        dispatch(CourseListMessage.SetLoading(true))
                        launch {
                            selectedCourseRepository.getSelectedCourses()
                                .flatMapLatest { ids ->
                                    // Получаем курсы пользователя
                                    courseRepository.getCoursesByIds(ids)
                                        .combine(courseRepository.getAllCourses()) { myCourses, allCourses ->
                                            // Вычитаем выбранные из общего списка
                                            val otherCourses = allCourses.filterNot { course -> myCourses.any { it.id == course.id } }
                                            Pair(myCourses, otherCourses)
                                        }
                                }
                                .collect { (myCourses, otherCourses) ->
                                    dispatch(CourseListMessage.SetMyCourses(myCourses))
                                    dispatch(CourseListMessage.SetAllCourses(otherCourses))
                                }
                        }
                    }
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
                        is CourseListMessage.SetMyCourses -> copy(myCourses = msg.myCourses, isLoading = false)
                        is CourseListMessage.SetAllCourses -> copy(allCourses = msg.allCourses, isLoading = false)
                        is CourseListMessage.SetLoading -> copy(isLoading = msg.isLoading)
                        is CourseListMessage.SetError -> copy(error = msg.error)
                    }
                }
            ) {}
}
