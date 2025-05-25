package ru.naumov.androidstepper.data

import ru.naumov.androidstepper.data.database.SelectedCourseDao
import ru.naumov.androidstepper.data.database.SelectedCourseEntity


class SelectedCourseRepository(
    private val selectedCourseDao: SelectedCourseDao
) {
    suspend fun selectCourse(courseId: String) {
        selectedCourseDao.insert(SelectedCourseEntity(courseId))
    }

    suspend fun unselectCourse(courseId: String) {
        val entity = selectedCourseDao.getCourse(courseId)
        if (entity != null) selectedCourseDao.delete(entity)
    }

    suspend fun getSelectedCourses(): List<String> =
        selectedCourseDao.getAll().map { it.courseId }

    suspend fun setSelectedCourses(ids: List<String>) {
        // Очищаем старое, записываем новое (или другой update-алгоритм)
        selectedCourseDao.clearAll()
        ids.forEach { selectedCourseDao.insert(SelectedCourseEntity(it)) }
    }
}
