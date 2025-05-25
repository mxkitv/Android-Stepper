package ru.naumov.androidstepper.test

val stateToModel: (TestState) -> TestComponent.TestModel = { state ->
    TestComponent.TestModel(
        questions = state.questions.map { q ->
            Question(
                id = q.id,
                text = q.text,
                answers = q.answers,
                correctAnswer = q.correctAnswer
            )
        },
        answers = state.answers,
        isLoading = state.isLoading,
        error = state.error,
        showResult = state.showResult,
        correctCount = state.correctCount
    )
}
