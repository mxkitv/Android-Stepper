// data/repository/MaterialRepository.kt
package ru.naumov.androidstepper.data

import ru.naumov.androidstepper.data.database.MaterialDao
import ru.naumov.androidstepper.data.database.MaterialEntity

class MaterialRepository(private val dao: MaterialDao) {
    suspend fun getMaterialByTopic(topicId: String): MaterialEntity? =
        dao.getMaterialByTopicId(topicId)
}
