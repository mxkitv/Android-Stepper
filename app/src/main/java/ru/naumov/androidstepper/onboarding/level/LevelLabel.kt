package ru.naumov.androidstepper.onboarding.level

sealed interface LevelLabel {
    object NavigateNext : LevelLabel
    object NavigateBack : LevelLabel
}