package ru.naumov.androidstepper.onboarding.course

sealed interface CourseLabel {
    object NavigateNext : CourseLabel
    object NavigateBack : CourseLabel
}
