package ru.naumov.androidstepper.onboarding.level

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class LevelStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): LevelStore =
        object : LevelStore,
            Store<LevelIntent, LevelState, LevelLabel> by storeFactory.create<LevelIntent, Nothing, LevelMessage, LevelState, LevelLabel>(
                name = "LevelStore",
                initialState = LevelState(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<LevelIntent.LevelSelected> { intent ->
                        dispatch(LevelMessage.LevelSelected(intent.level))
                        // Можно сбрасывать ошибку, если она была
                        dispatch(LevelMessage.SetError(null))
                    }
                    onIntent<LevelIntent.ContinueClicked> {
                        val selected = state().selectedLevel
                        if (selected == null) {
                            dispatch(LevelMessage.SetError("level_error_choose_level"))
                        } else {
                            dispatch(LevelMessage.SetLoading(true))
                            // Сохраняем/отправляем на сервер и т.д.
                            publish(LevelLabel.NavigateNext)
                            dispatch(LevelMessage.SetLoading(false))
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
                            error = msg.value
                        )
                    }
                }
            ) {}
}
