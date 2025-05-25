package ru.naumov.androidstepper.onboarding.username

import ru.naumov.androidstepper.onboarding.username.UsernameComponent.UsernameModel

internal val stateToModel: (UsernameState) -> UsernameModel =
    {
        UsernameModel(
            username = it.username,
            isLoading = it.isLoading,
            error = it.error
        )
    }
