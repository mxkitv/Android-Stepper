package ru.naumov.androidstepper.coursedetail

sealed interface CourseDetailIntent {
    object BackClicked : CourseDetailIntent
    object AddCourseClicked : CourseDetailIntent
}
