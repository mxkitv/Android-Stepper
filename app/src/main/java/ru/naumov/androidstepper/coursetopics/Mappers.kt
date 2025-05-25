// File: ru/naumov/androidstepper/coursetopics/StateToModelMapper.kt
package ru.naumov.androidstepper.coursetopics

import ru.naumov.androidstepper.coursetopics.CourseTopicsComponent.CourseTopicsModel

internal val stateToModel: (CourseTopicsState) -> CourseTopicsModel = { state ->
    CourseTopicsModel(
        courseId = state.courseId,
        courseTitle = state.courseTitle,
        topics = state.topics,
        completedCount = state.completedCount,
        totalCount = state.totalCount,
        isLoading = state.isLoading,
        error = state.error
    )
}
