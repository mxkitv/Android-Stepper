package ru.naumov.androidstepper.coursedetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
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
fun CourseDetailScreen(component: CourseDetailComponent) {
    val model by component.model.subscribeAsState()
    CourseDetailScreenContent(
        course = model.course,
        isLoading = model.isLoading,
        error = model.error,
        onBack = component::onBackClicked,
        onAddCourse = component::onAddCourseClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreenContent(
    course: CourseDetailItem?,
    isLoading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onAddCourse: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.course_detail_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.course_detail_back))
                    }
                },
                actions = {
                    Button(
                        onClick = onAddCourse,
                        enabled = !isLoading && course != null
                    ) {
                        Text(stringResource(R.string.course_detail_add))
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            course != null -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.course_detail_topics_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    if (course.topics.isEmpty()) {
                        Text(
                            text = stringResource(R.string.course_detail_topics_empty),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            course.topics.forEach { topic ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Face,
                                            contentDescription = topic.title,
                                            modifier = Modifier.size(28.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Text(
                                            text = topic.title,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CourseDetailScreenContentPreview() {
    val exampleCourse = CourseDetailItem(
        id = "course1",
        title = "Основы Android",
        description = "Научитесь создавать приложения под Android с нуля.",
        topics = listOf(
            TopicItem(id = "topic1", title = "Введение в Android"),
            TopicItem(id = "topic2", title = "Основы Kotlin"),
            TopicItem(id = "topic3", title = "UI и Activity"),
        )
    )
    MaterialTheme {
        CourseDetailScreenContent(
            course = exampleCourse,
            isLoading = false,
            error = null,
            onBack = {},
            onAddCourse = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun CourseDetailScreenContentPreview_Loading() {
    MaterialTheme {
        CourseDetailScreenContent(
            course = null,
            isLoading = true,
            error = null,
            onBack = {},
            onAddCourse = {}
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun CourseDetailScreenContentPreview_Error() {
    MaterialTheme {
        CourseDetailScreenContent(
            course = null,
            isLoading = false,
            error = "Ошибка загрузки данных",
            onBack = {},
            onAddCourse = {}
        )
    }
}
