package ru.naumov.androidstepper.test

val stateToModel: (TestState) -> TestComponent.TestModel = { state ->
    TestComponent.TestModel(
        questions = state.questions,
        answers = state.answers,
        isLoading = state.isLoading,
        error = state.error,
        showResult = state.showResult,
        correctCount = state.correctCount
    )
}
