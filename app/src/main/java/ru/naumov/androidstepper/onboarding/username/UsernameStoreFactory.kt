package ru.naumov.androidstepper.onboarding.username

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory

class UsernameStoreFactory(
    private val storeFactory: StoreFactory
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
                        val isValid = username.matches(
                            Regex("""^[\p{L}\p{N}_\- .!@#$%^&*()+=:;'"?/<>{}\[\]|~`]{2,24}$""")
                        )
                        if (!isValid) {
                            dispatch(
                                UsernameMessage.SetError("От 2 до 24 символов, можно буквы, цифры, спецсимволы.")
                            )
                        } else {
                            dispatch(UsernameMessage.SetLoading(true))
                            publish(UsernameLabel.NavigateNext)
                            dispatch(UsernameMessage.SetLoading(false))
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
                            error = msg.value
                        )
                    }
                }
            ) {}
}
