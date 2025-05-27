package ru.naumov.androidstepper.courses

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.badoo.reaktive.base.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.naumov.androidstepper.asValue
import ru.naumov.androidstepper.data.CourseRepository
import ru.naumov.androidstepper.data.SelectedCourseRepository

class CourseListComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<CourseListComponent.Output>
) : CourseListComponent, ComponentContext by componentContext, KoinComponent {

    private val selectedCourseRepository: SelectedCourseRepository by inject()
    private val courseRepository: CourseRepository by inject()

    private val store = instanceKeeper.getStore {
        CourseListStoreFactory(
            storeFactory = storeFactory,
            selectedCourseRepository = selectedCourseRepository,
            courseRepository = courseRepository
        ).create()
    }

    override val model: Value<CourseListComponent.Model> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    is CourseListLabel.OpenMyCourse -> output.onNext(
                        CourseListComponent.Output.OpenMyCourse(
                            label.courseId
                        )
                    )

                    is CourseListLabel.OpenCourseDetail -> output.onNext(
                        CourseListComponent.Output.OpenCourseDetail(
                            label.courseId
                        )
                    )

                    is CourseListLabel.ShowMessage -> output.onNext(
                        CourseListComponent.Output.ShowMessage(
                            label.message
                        )
                    )
                }
            }
            .launchIn(scope)
    }

    override fun onMyCourseClicked(courseId: String) {
        store.accept(CourseListIntent.MyCourseClicked(courseId))
    }

    override fun onCourseClicked(courseId: String) {
        store.accept(CourseListIntent.CourseClicked(courseId))
    }

    override fun onAddCourse(courseId: String) {
        store.accept(CourseListIntent.AddCourse(courseId))
    }
}
