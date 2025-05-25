package ru.naumov.androidstepper.courses

import ru.naumov.androidstepper.courses.CourseListComponent.Model

internal val stateToModel: (CourseListState) -> Model = { state ->
    Model(
        myCourses = state.myCourses,
        allCourses = state.allCourses,
        isLoading = state.isLoading,
        error = state.error
    )
}
