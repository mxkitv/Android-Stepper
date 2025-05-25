package ru.naumov.androidstepper.coursedetail

data class CourseDetailItem(
    val id: String,
    val title: String,
    val description: String,
    val topics: List<TopicItem>
)

data class TopicItem(
    val id: String,
    val title: String,
    val iconRes: Int? = null
)
