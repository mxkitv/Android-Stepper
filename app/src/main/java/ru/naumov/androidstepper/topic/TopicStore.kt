package ru.naumov.androidstepper.topic

import com.arkivanov.mvikotlin.core.store.Store

interface TopicStore : Store<TopicIntent, TopicState, TopicLabel>
