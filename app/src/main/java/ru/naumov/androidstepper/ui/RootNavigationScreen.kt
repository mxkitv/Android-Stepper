package ru.naumov.androidstepper.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import ru.naumov.androidstepper.courses.CourseListComponent
import ru.naumov.androidstepper.home.HomeScreen
import ru.naumov.androidstepper.courses.CourseListScreen
import ru.naumov.androidstepper.home.HomeComponent
import ru.naumov.androidstepper.ui.navigation.BottomNavItem

@Composable
fun RootNavigationScreen(
    homeComponent: HomeComponent,
    courseListComponent: CourseListComponent,
    startDestination: BottomNavItem = BottomNavItem.HOME
) {
    var selectedTab by remember { mutableStateOf(startDestination) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavItem.values().forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item,
                        onClick = { selectedTab = item },
                        icon = {
                            Icon(item.icon, contentDescription = item.label)
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier
            .fillMaxSize()
            .padding(padding)) {
            when (selectedTab) {
                BottomNavItem.HOME -> HomeScreen(homeComponent)
                BottomNavItem.COURSES -> CourseListScreen(courseListComponent)
            }
        }
    }
}
