package ru.naumov.androidstepper.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics WHERE courseId = :courseId")
    suspend fun getTopicsForCourse(courseId: String): List<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topics: List<TopicEntity>)
}
