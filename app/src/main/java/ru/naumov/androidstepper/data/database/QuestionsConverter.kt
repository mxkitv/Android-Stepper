package ru.naumov.androidstepper.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object TestJsonParser {
    val json = Json { ignoreUnknownKeys = true }
}

class QuestionsConverter {
    @TypeConverter
    fun fromQuestions(questions: List<Question>?): String =
        TestJsonParser.json.encodeToString(questions ?: emptyList())

    @TypeConverter
    fun toQuestions(data: String?): List<Question> =
        if (data.isNullOrEmpty()) emptyList()
        else TestJsonParser.json.decodeFromString(data)
}
