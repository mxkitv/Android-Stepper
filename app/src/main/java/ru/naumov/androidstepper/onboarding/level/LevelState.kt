package ru.naumov.androidstepper.onboarding.level

data class LevelState(
    val selectedLevel: UserLevel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)