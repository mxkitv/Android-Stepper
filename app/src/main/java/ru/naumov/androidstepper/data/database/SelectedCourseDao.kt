package ru.naumov.androidstepper.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete

@Dao
interface SelectedCourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: SelectedCourseEntity)

    @Delete
    suspend fun delete(course: SelectedCourseEntity)

    @Query("SELECT * FROM selected_courses")
    suspend fun getAll(): List<SelectedCourseEntity>

    @Query("SELECT * FROM selected_courses WHERE courseId = :courseId LIMIT 1")
    suspend fun getCourse(courseId: String): SelectedCourseEntity?

    @Query("DELETE FROM selected_courses")
    suspend fun clearAll()
}
