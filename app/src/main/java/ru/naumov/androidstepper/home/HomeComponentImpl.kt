package ru.naumov.androidstepper.home

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

class HomeComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<HomeComponent.Output>
) : HomeComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            HomeStoreFactory(storeFactory).create()
        }

    override val model: Value<HomeComponent.HomeModel> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    is HomeLabel.OpenCourse -> output.onNext(HomeComponent.Output.OpenCourse(label.courseId))
                    is HomeLabel.OpenRecent -> output.onNext(HomeComponent.Output.OpenRecent(label.courseId, label.topicId))
                }
            }
            .launchIn(scope)
    }

    override fun onCourseClicked(courseId: String) {
        store.accept(HomeIntent.CourseClicked(courseId))
    }

    override fun onRecentClicked(courseId: String, topicId: String) {
        store.accept(HomeIntent.RecentClicked(courseId, topicId))
    }
}
