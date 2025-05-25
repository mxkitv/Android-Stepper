package ru.naumov.androidstepper.onboarding.username

sealed interface UsernameLabel {
    object NavigateNext : UsernameLabel
    object NavigateBack : UsernameLabel
}