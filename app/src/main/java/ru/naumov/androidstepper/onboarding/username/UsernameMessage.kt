package ru.naumov.androidstepper.onboarding.username

sealed interface UsernameMessage {
    data class UsernameChanged(val value: String) : UsernameMessage
    data class SetLoading(val value: Boolean) : UsernameMessage
    data class SetError(val value: String?) : UsernameMessage
}