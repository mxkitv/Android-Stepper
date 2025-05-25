package ru.naumov.androidstepper.onboarding.course

sealed interface CourseAction {
    object LoadCourses : CourseAction
}