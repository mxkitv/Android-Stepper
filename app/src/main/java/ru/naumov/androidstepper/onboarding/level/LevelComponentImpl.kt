package ru.naumov.androidstepper.onboarding.level

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.badoo.reaktive.base.Consumer
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.naumov.androidstepper.asValue
import ru.naumov.androidstepper.data.UserRepository
import kotlin.getValue

class LevelComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<LevelComponent.LevelOutput>
) : LevelComponent, ComponentContext by componentContext, KoinComponent {

    private val userRepository: UserRepository by inject()

    private val store =
        instanceKeeper.getStore {
            LevelStoreFactory(
                storeFactory = storeFactory,
                userRepository = userRepository
            ).create()
        }

    override val model: Value<LevelComponent.LevelModel> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    LevelLabel.NavigateNext -> output.onNext(LevelComponent.LevelOutput.NavigateNext)
                    LevelLabel.NavigateBack -> output.onNext(LevelComponent.LevelOutput.NavigateBack)
                }
            }
            .launchIn(scope)
    }

    override fun onLevelSelected(level: UserLevel) {
        store.accept(LevelIntent.LevelSelected(level))
    }

    override fun onContinue() {
        store.accept(LevelIntent.ContinueClicked)
    }

    override fun onBack() {
        store.accept(LevelIntent.BackClicked)
    }
}
