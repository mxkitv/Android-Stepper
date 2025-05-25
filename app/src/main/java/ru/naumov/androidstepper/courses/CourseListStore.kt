package ru.naumov.androidstepper.courses

import com.arkivanov.mvikotlin.core.store.Store

interface CourseListStore : Store<CourseListIntent, CourseListState, CourseListLabel>
