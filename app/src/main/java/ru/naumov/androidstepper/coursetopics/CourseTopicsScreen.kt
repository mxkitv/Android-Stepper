// File: ru/naumov/androidstepper/coursetopics/CourseTopicsScreen.kt
package ru.naumov.androidstepper.coursetopics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.naumov.androidstepper.R

@Composable
fun CourseTopicsScreen(component: CourseTopicsComponent) {
    val model by component.model.subscribeAsState()
    CourseTopicsScreenContent(
        courseTitle = model.courseTitle,
        topics = model.topics,
        isLoading = model.isLoading,
        error = model.error,
        onBackClicked = component::onBackClicked,
        onTopicClicked = component::onTopicClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseTopicsScreenContent(
    courseTitle: String,
    topics: List<CourseTopicItem>,
    isLoading: Boolean,
    error: String?,
    onBackClicked: () -> Unit,
    onTopicClicked: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(courseTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.course_topics_nav_back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Прогресс по курсу
            val completed = topics.count { it.isCompleted }
            val all = topics.size
            if (topics.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.course_topics_progress_label, completed, all),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Ошибка
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Лоадер
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Список тем
                topics.forEach { topic ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { onTopicClicked(topic.id) },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = topic.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                                if (topic.isCompleted) {
                                    Text(
                                        text = stringResource(R.string.course_topics_completed),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                            if (topic.progress != null) {
                                Spacer(Modifier.width(12.dp))
                                LinearProgressIndicator(
                                    progress = topic.progress.coerceIn(0f, 1f),
                                    modifier = Modifier
                                        .width(64.dp)
                                        .height(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------ Preview Models & Preview ------
@Preview(showBackground = true)
@Composable
private fun CourseTopicsScreenContentPreview() {
    CourseTopicsScreenContent(
        courseTitle = "Jetpack Compose",
        topics = listOf(
            CourseTopicItem(id = "t1", title = "Введение", isCompleted = true, progress = 1f),
            CourseTopicItem(id = "t2", title = "Layouts", isCompleted = false, progress = 0.3f),
            CourseTopicItem(id = "t3", title = "Navigation", isCompleted = false, progress = null),
        ),
        isLoading = false,
        error = null,
        onBackClicked = {},
        onTopicClicked = {}
    )
}
