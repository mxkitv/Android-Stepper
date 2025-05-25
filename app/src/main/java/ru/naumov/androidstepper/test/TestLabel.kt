package ru.naumov.androidstepper.test

sealed interface TestLabel {
    object NavigateBack : TestLabel
    object ShowResult : TestLabel   // Можно расширить в будущем, например, с результатом или флагом passed
    data class ShowMessage(val message: String) : TestLabel  // Для будущих ошибок/подсказок
}
