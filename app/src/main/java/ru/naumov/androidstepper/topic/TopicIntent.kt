package ru.naumov.androidstepper.topic

sealed interface TopicIntent {
    object BackClicked : TopicIntent
    object TestClicked : TopicIntent
    data class ProgressChanged(val progress: Float) : TopicIntent // 0.0..1.0 — обновление при скролле
}
