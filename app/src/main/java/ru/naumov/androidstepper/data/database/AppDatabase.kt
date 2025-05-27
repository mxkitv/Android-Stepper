package ru.naumov.androidstepper.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CourseEntity::class, TopicEntity::class, MaterialEntity::class, SelectedCourseEntity::class, TestEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    QuestionsConverter::class // <--- Добавь этот класс!
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedCourseDao(): SelectedCourseDao
    abstract fun courseDao(): CourseDao
    abstract fun topicDao(): TopicDao
    abstract fun materialDao(): MaterialDao
    abstract fun testDao(): TestDao
}
