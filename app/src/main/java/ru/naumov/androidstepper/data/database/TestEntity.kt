package ru.naumov.androidstepper.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@JsonIgnoreUnknownKeys
@Entity(tableName = "tests")
@Serializable
data class TestEntity(
    @PrimaryKey val id: String,
    val topicId: String,
    val title: String,
    @TypeConverters(QuestionsConverter::class)
    val questions: List<Question>
)

@Serializable
data class Question(
    val id: String,
    val text: String,
    val answers: List<String>,
    val correctAnswer: Int
)
