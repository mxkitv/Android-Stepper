package ru.naumov.androidstepper.coursedetail

import ru.naumov.androidstepper.coursedetail.CourseDetailComponent.CourseDetailModel

internal val stateToModel: (CourseDetailState) -> CourseDetailModel = { state ->
    CourseDetailModel(
        course = state.course,
        isLoading = state.isLoading,
        error = state.error
    )
}
