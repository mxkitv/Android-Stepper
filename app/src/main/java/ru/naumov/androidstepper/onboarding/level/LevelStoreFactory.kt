package ru.naumov.androidstepper.onboarding.level

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.UserRepository

class LevelStoreFactory(
    private val storeFactory: StoreFactory,
    private val userRepository: UserRepository
) {
    fun create(): LevelStore =
        object : LevelStore,
            Store<LevelIntent, LevelState, LevelLabel> by storeFactory.create<LevelIntent, Nothing, LevelMessage, LevelState, LevelLabel>(
                name = "LevelStore",
                initialState = LevelState(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<LevelIntent.LevelSelected> { intent ->
                        dispatch(LevelMessage.LevelSelected(intent.level))
                        dispatch(LevelMessage.SetError(null))
                    }
                    onIntent<LevelIntent.ContinueClicked> { it: LevelIntent.ContinueClicked ->
                        val level = state().selectedLevel
                        if (level != null) {
                            dispatch(LevelMessage.SetLoading(true))
                            launch {
                                userRepository.saveUserLevel(level.name)
                                publish(LevelLabel.NavigateNext)
                            }
                        } else {
                            dispatch(LevelMessage.SetError("Пожалуйста, выберите уровень"))
                        }
                    }
                    onIntent<LevelIntent.BackClicked> {
                        publish(LevelLabel.NavigateBack)
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is LevelMessage.LevelSelected -> copy(
                            selectedLevel = msg.level
                        )
                        is LevelMessage.SetLoading -> copy(
                            isLoading = msg.value
                        )
                        is LevelMessage.SetError -> copy(
                            error = msg.value, isLoading = false
                        )
                    }
                }
            ) {}
}
