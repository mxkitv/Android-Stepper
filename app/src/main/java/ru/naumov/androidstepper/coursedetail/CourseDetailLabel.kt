package ru.naumov.androidstepper.coursedetail

sealed interface CourseDetailLabel {
    data class ShowMessage(val message: String) : CourseDetailLabel
    // Можно добавить другие label-ы, если понадобится
}
