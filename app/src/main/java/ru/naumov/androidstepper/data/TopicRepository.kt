// data/repository/TopicRepository.kt
package ru.naumov.androidstepper.data

import ru.naumov.androidstepper.data.database.TopicDao
import ru.naumov.androidstepper.data.database.TopicEntity

class TopicRepository(private val dao: TopicDao) {
    suspend fun getTopicsByCourse(courseId: String): List<TopicEntity> =
        dao.getTopicsForCourse(courseId)
}
