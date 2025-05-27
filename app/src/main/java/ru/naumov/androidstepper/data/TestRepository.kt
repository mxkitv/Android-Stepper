package ru.naumov.androidstepper.data

import kotlinx.coroutines.flow.Flow
import ru.naumov.androidstepper.data.database.TestDao
import ru.naumov.androidstepper.data.database.TestEntity

class TestRepository(private val testDao: TestDao) {
    fun getTestByTopicId(topicId: String): Flow<TestEntity?> =
        testDao.getTestByTopicId(topicId)

    suspend fun addTests(tests: List<TestEntity>) {
        testDao.insertAll(tests)
    }
}
