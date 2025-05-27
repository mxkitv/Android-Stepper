package ru.naumov.androidstepper.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(
    val label: String,
    val icon: ImageVector
) {
    HOME("Главная", Icons.Filled.Home),
    COURSES("Курсы", Icons.Filled.Menu)
}
