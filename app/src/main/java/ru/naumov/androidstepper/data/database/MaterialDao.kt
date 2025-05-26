package ru.naumov.androidstepper.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MaterialDao {
    @Query("SELECT * FROM materials WHERE topicId = :topicId")
    suspend fun getMaterialsForTopic(topicId: String): List<MaterialEntity>

    @Query("SELECT * FROM materials WHERE topicId = :topicId LIMIT 1")
    suspend fun getMaterialByTopicId(topicId: String): MaterialEntity?

    @Query("SELECT * FROM materials WHERE id = :id")
    suspend fun getMaterialById(id: String): MaterialEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(materials: List<MaterialEntity>)
}
