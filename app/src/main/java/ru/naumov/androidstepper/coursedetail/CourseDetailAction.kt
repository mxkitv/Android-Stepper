package ru.naumov.androidstepper.coursedetail

sealed interface CourseDetailAction {
    object LoadCourse : CourseDetailAction
}