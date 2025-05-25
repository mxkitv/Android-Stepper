package ru.naumov.androidstepper.data

import ru.naumov.androidstepper.data.database.CourseDao
import ru.naumov.androidstepper.data.database.CourseEntity

class CourseRepository(private val dao: CourseDao) {
    suspend fun getAllCourses(): List<CourseEntity> = dao.getAllCourses()
}