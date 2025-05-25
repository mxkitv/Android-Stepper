package ru.naumov.androidstepper.onboarding.course

import ru.naumov.androidstepper.onboarding.course.CourseComponent.CourseModel

internal val stateToModel: (CourseState) -> CourseModel = { state ->
    CourseModel(
        selectedCourseIds = state.selectedCourseIds,
        isLoading = state.isLoading,
        error = state.error
    )
}
