package ru.naumov.androidstepper.home

import ru.naumov.androidstepper.home.HomeComponent.HomeModel

internal val stateToModel: (HomeState) -> HomeModel = { state ->
    HomeModel(
        username = state.username,
        courses = state.courses,
        recentEvents = state.recentEvents,
        progress = state.progress,
        isLoading = state.isLoading,
        error = state.error
    )
}
