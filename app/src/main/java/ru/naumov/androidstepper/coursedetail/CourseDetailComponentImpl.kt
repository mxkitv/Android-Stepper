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
import ru.naumov.androidstepper.asValue

class CourseDetailComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<CourseDetailComponent.Output>
) : CourseDetailComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            CourseDetailStoreFactory(storeFactory).create()
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
        output.onNext(CourseDetailComponent.Output.CourseAdded)
    }
}
