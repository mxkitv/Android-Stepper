package ru.naumov.androidstepper.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.naumov.androidstepper.R
import ru.naumov.androidstepper.data.database.CourseEntity

@Composable
fun HomeScreen(component: HomeComponent) {
    val model by component.model.subscribeAsState()
    HomeScreenContent(
        username = model.username,
        courses = model.courses,
        progress = model.progress,
        recentEvents = model.recentEvents,
        isLoading = model.isLoading,
        error = model.error,
        onCourseClicked = component::onCourseClicked,
        onRecentClicked = component::onRecentClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    username: String,
    courses: List<CourseEntity>,
    progress: Float,
    recentEvents: List<RecentEvent>,
    isLoading: Boolean,
    error: String?,
    onCourseClicked: (String) -> Unit,
    onRecentClicked: (String, String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_screen_title)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Приветствие
            Text(
                text = stringResource(R.string.home_greeting, username),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Общий прогресс
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.home_progress_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = progress.coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Список курсов
            Text(
                text = stringResource(R.string.home_courses_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            CoursesGrid(
                courses = courses,
                onCourseClicked = onCourseClicked
            )

            // Недавние действия
            if (recentEvents.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.home_recent_label),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                RecentEventsColumn(
                    events = recentEvents,
                    onRecentClicked = onRecentClicked
                )
            }

            // Ошибка
            if (error != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Лоадер (опционально, например в центре экрана)
            if (isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
private fun CoursesGrid(
    courses: List<CourseEntity>,
    onCourseClicked: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(courses) { course ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onCourseClicked(course.id) },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = course.title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentEventsColumn(
    events: List<RecentEvent>,
    onRecentClicked: (String, String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        events.forEach { event ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRecentClicked(event.courseId, event.id) },
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (event.iconRes != null) {
                        Icon(
                            painter = painterResource(id = event.iconRes),
                            contentDescription = event.title,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = event.title,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = event.courseTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // Можно добавить дату/время если потребуется
                }
            }
        }
    }
}

// --- Превью ---

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(
        username = "Алексей",
        courses = listOf(
            CourseEntity(id = "android", title = "Android", description = ""),
            CourseEntity(id = "compose", title = "Compose", description = ""),
            CourseEntity(id = "kotlin", title = "Kotlin", description = ""),
            CourseEntity(id = "mvi", title = "MVI", description = "")
        ),
        progress = 0.6f,
        recentEvents = listOf(
            RecentEvent(
                id = "t1",
                title = "Введение в Android",
                courseId = "android",
                courseTitle = "Android",
                iconRes = null,
                timestamp = System.currentTimeMillis()
            ),
            RecentEvent(
                id = "t2",
                title = "Layouts в Compose",
                courseId = "compose",
                courseTitle = "Compose",
                iconRes = null,
                timestamp = System.currentTimeMillis()
            )
        ),
        isLoading = false,
        error = null,
        onCourseClicked = {},
        onRecentClicked = { _, _ -> }
    )
}
