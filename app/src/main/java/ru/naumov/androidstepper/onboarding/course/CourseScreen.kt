package ru.naumov.androidstepper.onboarding.course

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import androidx.compose.ui.tooling.preview.Preview
import ru.naumov.androidstepper.data.database.CourseEntity

@Composable
fun CourseScreen(component: CourseComponent) {
    val model by component.model.subscribeAsState()
    CourseScreenContent(
        courses = model.courses,
        selectedCourseIds = model.selectedCourseIds,
        isLoading = model.isLoading,
        onCourseToggled = component::onCourseToggled,
        onContinue = component::onContinue,
        onBack = component::onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreenContent(
    courses: List<CourseEntity>,
    selectedCourseIds: Set<String>,
    isLoading: Boolean,
    onCourseToggled: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Выбор курса") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Выбери курсы",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Отметь один или несколько курсов, которые ты хочешь пройти.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(28.dp))
                CourseOptionsGrid(
                    courses = courses,
                    selectedCourseIds = selectedCourseIds,
                    onCourseToggled = onCourseToggled,
                    enabled = !isLoading
                )
                Spacer(Modifier.height(28.dp))
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            if (selectedCourseIds.isNotEmpty() && !isLoading)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onContinue,
                        enabled = selectedCourseIds.isNotEmpty() && !isLoading,
                        modifier = Modifier.size(56.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Продолжить",
                                tint = if (selectedCourseIds.isNotEmpty() && !isLoading)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CourseOptionsGrid(
    courses: List<CourseEntity>,
    selectedCourseIds: Set<String>,
    onCourseToggled: (String) -> Unit,
    enabled: Boolean
) {
    LazyHorizontalGrid (
        rows = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 360.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = true
    ) {
        items(courses) { course ->
            val isSelected = selectedCourseIds.contains(course.id)
            val bgColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            Surface(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(CircleShape)
                    .background(bgColor)
                    .clickable(enabled = enabled) {
                        onCourseToggled(course.id)
                    },
                tonalElevation = if (isSelected) 4.dp else 0.dp,
                shadowElevation = if (isSelected) 8.dp else 0.dp,
            ) {
                Column(
                    Modifier
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = course.title,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (course.description.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// --- Preview Section ---

private val previewCourses = listOf(
    CourseEntity("android_basics", "Основы Android", "Базовый курс для начинающих"),
    CourseEntity("jetpack_compose", "Jetpack Compose", "Современный UI в Android"),
    CourseEntity("kotlin_basics", "Kotlin", "Основы языка Kotlin"),
    CourseEntity("arch", "Архитектура", "Архитектурные паттерны Android")
)

@Preview(showBackground = true)
@Composable
private fun CourseScreenContentPreview_Default() {
    MaterialTheme {
        CourseScreenContent(
            courses = previewCourses,
            selectedCourseIds = emptySet(),
            isLoading = false,
            onCourseToggled = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Курс выбран")
@Composable
private fun CourseScreenContentPreview_Selected() {
    MaterialTheme {
        CourseScreenContent(
            courses = previewCourses,
            selectedCourseIds = setOf("android_basics", "jetpack_compose"),
            isLoading = false,
            onCourseToggled = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Загрузка")
@Composable
private fun CourseScreenContentPreview_Loading() {
    MaterialTheme {
        CourseScreenContent(
            courses = previewCourses,
            selectedCourseIds = setOf("android_basics"),
            isLoading = true,
            onCourseToggled = {},
            onContinue = {},
            onBack = {}
        )
    }
}
