package ru.naumov.androidstepper.onboarding.username

data class UsernameState(
    val username: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)