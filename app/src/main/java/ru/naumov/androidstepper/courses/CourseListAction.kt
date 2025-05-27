package ru.naumov.androidstepper.courses

sealed interface CourseListAction {
    object LoadCourses : CourseListAction
}