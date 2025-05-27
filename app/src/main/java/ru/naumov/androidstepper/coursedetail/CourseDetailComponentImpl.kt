package ru.naumov.androidstepper.coursedetail

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

class CourseDetailComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val courseId: String,
    private val output: Consumer<CourseDetailComponent.Output>
) : CourseDetailComponent, ComponentContext by componentContext, KoinComponent {

    private val courseRepository: CourseRepository by inject()
    private val selectedCourseRepository: SelectedCourseRepository by inject()

    private val store =
        instanceKeeper.getStore {
            CourseDetailStoreFactory(
                storeFactory = storeFactory,
                courseRepository = courseRepository,
                selectedCourseRepository = selectedCourseRepository
            ).create(courseId)
        }

    override val model: Value<CourseDetailComponent.CourseDetailModel> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    is CourseDetailLabel.ShowMessage -> {
                        // Можно обработать сообщение или уведомление, если нужно
                    }
                }
            }
            .launchIn(scope)
    }

    override fun onBackClicked() {
        output.onNext(CourseDetailComponent.Output.Back)
    }

    override fun onAddCourseClicked() {
        store.accept(CourseDetailIntent.AddCourseClicked)
    }
}
