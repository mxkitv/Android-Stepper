package ru.naumov.androidstepper.onboarding.username

sealed interface UsernameIntent {
    data class UsernameChanged(val value: String) : UsernameIntent
    object ContinueClicked : UsernameIntent
    object BackClicked : UsernameIntent
}