package ru.naumov.androidstepper.onboarding.level

sealed interface LevelMessage {
    data class LevelSelected(val level: UserLevel) : LevelMessage
    data class SetLoading(val value: Boolean) : LevelMessage
    data class SetError(val value: String?) : LevelMessage
}