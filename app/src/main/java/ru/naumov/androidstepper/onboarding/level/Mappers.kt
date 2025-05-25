package ru.naumov.androidstepper.onboarding.level

import ru.naumov.androidstepper.onboarding.level.LevelComponent.LevelModel

internal val stateToModel: (LevelState) -> LevelModel = {
    LevelModel(
        selectedLevel = it.selectedLevel,
        isLoading = it.isLoading,
        error = it.error
    )
}
