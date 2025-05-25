package ru.naumov.androidstepper.onboarding.level

sealed interface LevelIntent {
    data class LevelSelected(val level: UserLevel) : LevelIntent
    object ContinueClicked : LevelIntent
    object BackClicked : LevelIntent
}