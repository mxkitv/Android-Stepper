package ru.naumov.androidstepper.onboarding.course

import ru.naumov.androidstepper.onboarding.course.CourseComponent.CourseModel

internal val stateToModel: (CourseState) -> CourseModel = { state ->
    CourseModel(
        courses = state.courses,
        selectedCourseIds = state.selectedCourseIds,
        isLoading = state.isLoading,
    )
}
