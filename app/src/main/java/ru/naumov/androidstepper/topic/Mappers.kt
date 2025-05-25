package ru.naumov.androidstepper.topic

internal val stateToModel: (TopicState) -> TopicComponent.TopicModel = { state ->
    TopicComponent.TopicModel(
        content = state.content,
        images = state.images,
        progress = state.progress,
        isLoading = state.isLoading,
        error = state.error
    )
}