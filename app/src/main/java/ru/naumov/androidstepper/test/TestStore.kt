package ru.naumov.androidstepper.test

import com.arkivanov.mvikotlin.core.store.Store

interface TestStore : Store<TestIntent, TestState, TestLabel>
