package ru.naumov.androidstepper.onboarding.course

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
import ru.naumov.androidstepper.asValue

class CourseComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<CourseComponent.CourseOutput>
) : CourseComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            CourseStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    override val model: Value<CourseComponent.CourseModel> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    CourseLabel.NavigateNext -> output.onNext(CourseComponent.CourseOutput.NavigateNext)
                    CourseLabel.NavigateBack -> output.onNext(CourseComponent.CourseOutput.NavigateBack)
                }
            }
            .launchIn(scope)
    }

    override fun onCourseSelected(courseId: String) {
        store.accept(CourseIntent.CourseSelected(courseId))
    }

    override fun onCourseDeselected(courseId: String) {
        store.accept(CourseIntent.CourseDeselected(courseId))
    }

    override fun onContinue() {
        store.accept(CourseIntent.ContinueClicked)
    }

    override fun onBack() {
        store.accept(CourseIntent.BackClicked)
    }
}
