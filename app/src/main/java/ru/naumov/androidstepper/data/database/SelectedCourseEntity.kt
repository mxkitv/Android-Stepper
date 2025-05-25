package ru.naumov.androidstepper.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "selected_courses")
data class SelectedCourseEntity(
    @PrimaryKey val courseId: String
    // можешь добавить еще поля (название, timestamp и т.п.)
)
