package ru.naumov.androidstepper.loading

import ru.naumov.androidstepper.root.RootComponentImpl.Configuration

interface LoadingComponent {

    sealed interface LoadingOutput {
        data class onLoaded(val config: Configuration) : LoadingOutput
    }
}