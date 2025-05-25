package ru.naumov.androidstepper.onboarding.level

import com.arkivanov.decompose.value.Value

interface LevelComponent {
    val model: Value<LevelModel>

    fun onLevelSelected(level: UserLevel)
    fun onContinue()
    fun onBack()

    data class LevelModel(
        val selectedLevel: UserLevel? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface LevelOutput {
        object NavigateNext : LevelOutput
        object NavigateBack : LevelOutput
    }
}
