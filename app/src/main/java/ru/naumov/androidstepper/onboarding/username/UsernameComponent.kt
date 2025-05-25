package ru.naumov.androidstepper.onboarding.username

import com.arkivanov.decompose.value.Value

interface UsernameComponent {
    val model: Value<UsernameModel>

    fun onUsernameChange(newUsername: String)
    fun onContinue()
    fun onBack()

    data class UsernameModel(
        val username: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface UsernameOutput {
        object NavigateNext : UsernameOutput
        object NavigateBack : UsernameOutput
    }
}
