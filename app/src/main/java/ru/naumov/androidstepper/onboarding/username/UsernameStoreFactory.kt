package ru.naumov.androidstepper.onboarding.username

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch
import ru.naumov.androidstepper.data.UserRepository

class UsernameStoreFactory(
    private val storeFactory: StoreFactory,
    private val userRepository: UserRepository
) {
    fun create(): UsernameStore =
        object : UsernameStore,
            Store<UsernameIntent, UsernameState, UsernameLabel> by storeFactory.create<UsernameIntent, Nothing, UsernameMessage, UsernameState, UsernameLabel>(
                name = "UsernameStore",
                initialState = UsernameState(),
                executorFactory = coroutineExecutorFactory {
                    onIntent<UsernameIntent.UsernameChanged> { intent ->
                        dispatch(UsernameMessage.UsernameChanged(value = intent.value))
                        dispatch(UsernameMessage.SetError(null))
                    }
                    onIntent<UsernameIntent.ContinueClicked> {
                        val username = state().username.trim()
                        if (username.length in 2..24) {
                            dispatch(UsernameMessage.SetLoading(true))
                            launch {
                                userRepository.saveUsername(username)
                                publish(UsernameLabel.NavigateNext)
                            }
                        } else {
                            dispatch(UsernameMessage.SetError("Имя должно содержать от 2 до 24 символов"))
                        }
                    }
                    onIntent<UsernameIntent.BackClicked> {
                        publish(UsernameLabel.NavigateBack)
                    }
                },
                reducer = { msg ->
                    when (msg) {
                        is UsernameMessage.UsernameChanged -> copy(
                            username = msg.value
                        )

                        is UsernameMessage.SetLoading -> copy(
                            isLoading = msg.value
                        )

                        is UsernameMessage.SetError -> copy(
                            error = msg.value,
                            isLoading = false
                        )
                    }
                }
            ) {}
}
