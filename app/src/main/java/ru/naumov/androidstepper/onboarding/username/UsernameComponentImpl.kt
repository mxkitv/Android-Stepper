package ru.naumov.androidstepper.onboarding.username

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.badoo.reaktive.base.Consumer
import kotlinx.coroutines.flow.onEach
import ru.naumov.androidstepper.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn

class UsernameComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<UsernameComponent.UsernameOutput>
) : UsernameComponent, ComponentContext by componentContext {
    private val store =
        instanceKeeper.getStore {
            UsernameStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    override val model: Value<UsernameComponent.UsernameModel> = store.asValue().map(stateToModel)

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    UsernameLabel.NavigateNext -> output.onNext(UsernameComponent.UsernameOutput.NavigateNext)
                    UsernameLabel.NavigateBack -> output.onNext(UsernameComponent.UsernameOutput.NavigateBack)
                }
            }
            .launchIn(scope)
    }

    override fun onUsernameChange(newUsername: String) {
        store.accept(UsernameIntent.UsernameChanged(value = newUsername))
    }

    override fun onContinue() {
        store.accept(UsernameIntent.ContinueClicked)
    }

    override fun onBack() {
        store.accept(UsernameIntent.BackClicked)
    }
}
