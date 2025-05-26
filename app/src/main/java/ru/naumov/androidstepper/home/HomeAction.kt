package ru.naumov.androidstepper.home

sealed interface HomeAction {
    object LoadMyCourses : HomeAction
}