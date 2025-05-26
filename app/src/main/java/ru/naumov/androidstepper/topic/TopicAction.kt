package ru.naumov.androidstepper.topic

sealed interface TopicAction {
    object LoadMaterial : TopicAction
}