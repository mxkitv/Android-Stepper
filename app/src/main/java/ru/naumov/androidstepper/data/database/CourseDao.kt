package ru.naumov.androidstepper.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    suspend fun getAllCourses(): List<CourseEntity>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourseById(id: String): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(courses: List<CourseEntity>)

    @Query("SELECT * FROM courses WHERE id IN (:ids)")
    suspend fun getCoursesByIds(ids: List<String>): List<CourseEntity>
}
