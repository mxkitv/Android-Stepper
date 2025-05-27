package ru.naumov.androidstepper.data

import kotlinx.coroutines.flow.Flow
import ru.naumov.androidstepper.data.database.CourseDao
import ru.naumov.androidstepper.data.database.CourseEntity

class CourseRepository(private val dao: CourseDao) {
    suspend fun getAllCourses(): Flow<List<CourseEntity>> = dao.getAllCourses()

    suspend fun getCoursesByIds(ids: List<String>): Flow<List<CourseEntity>> = dao.getCoursesByIds(ids)
}