package ru.naumov.androidstepper.onboarding.course

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.naumov.androidstepper.R

// Пример списка доступных курсов (может быть вынесен в отдельный файл/слой)
private val availableCourses = listOf(
    CourseInfo("android_basics", R.string.course_android_basics),
    CourseInfo("compose", R.string.course_compose),
    CourseInfo("architecture", R.string.course_architecture),
)

data class CourseInfo(val id: String, val titleRes: Int)

@Composable
fun CourseScreen(component: CourseComponent) {
    val model by component.model.subscribeAsState()
    CourseScreenContent(
        selectedCourseIds = model.selectedCourseIds,
        isLoading = model.isLoading,
        error = model.error,
        onCourseSelected = component::onCourseSelected,
        onCourseDeselected = component::onCourseDeselected,
        onContinue = component::onContinue,
        onBack = component::onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreenContent(
    selectedCourseIds: Set<String>,
    isLoading: Boolean,
    error: String?,
    onCourseSelected: (String) -> Unit,
    onCourseDeselected: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.course_screen_title_appbar)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.course_screen_nav_back)
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
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.course_screen_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.course_screen_instruction),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(28.dp))
                CourseOptions(
                    courses = availableCourses,
                    selectedCourseIds = selectedCourseIds,
                    onCourseSelected = onCourseSelected,
                    onCourseDeselected = onCourseDeselected,
                    enabled = !isLoading
                )
                val shouldShowError = error != null
                if (shouldShowError) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (error == "course_error_choose")
                            stringResource(R.string.course_error_choose)
                        else
                            error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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
                                contentDescription = stringResource(R.string.course_screen_continue),
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
private fun CourseOptions(
    courses: List<CourseInfo>,
    selectedCourseIds: Set<String>,
    onCourseSelected: (String) -> Unit,
    onCourseDeselected: (String) -> Unit,
    enabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        courses.forEach { course ->
            val isSelected = selectedCourseIds.contains(course.id)
            val bgColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(bgColor)
                    .clickable(enabled = enabled) {
                        if (!isSelected) onCourseSelected(course.id)
                        else onCourseDeselected(course.id)
                    },
                tonalElevation = if (isSelected) 4.dp else 0.dp,
                shadowElevation = if (isSelected) 8.dp else 0.dp,
            ) {
                Text(
                    text = stringResource(course.titleRes),
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 32.dp)
                        .fillMaxWidth(),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// --- Preview Section ---

@Preview(showBackground = true)
@Composable
private fun CourseScreenContentPreview_Default() {
    MaterialTheme {
        CourseScreenContent(
            selectedCourseIds = emptySet(),
            isLoading = false,
            error = null,
            onCourseSelected = {},
            onCourseDeselected = {},
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
            selectedCourseIds = setOf("android_basics", "compose"),
            isLoading = false,
            error = null,
            onCourseSelected = {},
            onCourseDeselected = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Ошибка")
@Composable
private fun CourseScreenContentPreview_Error() {
    MaterialTheme {
        CourseScreenContent(
            selectedCourseIds = emptySet(),
            isLoading = false,
            error = "course_error_choose",
            onCourseSelected = {},
            onCourseDeselected = {},
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
            selectedCourseIds = setOf("android_basics"),
            isLoading = true,
            error = null,
            onCourseSelected = {},
            onCourseDeselected = {},
            onContinue = {},
            onBack = {}
        )
    }
}
