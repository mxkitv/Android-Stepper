package ru.naumov.androidstepper.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CourseEntity::class, TopicEntity::class, MaterialEntity::class, SelectedCourseEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedCourseDao(): SelectedCourseDao
    abstract fun courseDao(): CourseDao
    abstract fun topicDao(): TopicDao
    abstract fun materialDao(): MaterialDao
}
