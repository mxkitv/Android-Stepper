package ru.naumov.androidstepper.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.naumov.androidstepper.R
import ru.naumov.androidstepper.data.database.CourseEntity
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CourseListScreen(component: CourseListComponent) {
    val model by component.model.subscribeAsState()
    CourseListScreenContent(
        myCourses = model.myCourses,
        allCourses = model.allCourses,
        isLoading = model.isLoading,
        error = model.error,
        onMyCourseClick = component::onMyCourseClicked,
        onCourseClick = component::onCourseClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreenContent(
    myCourses: List<CourseEntity>,
    allCourses: List<CourseEntity>,
    isLoading: Boolean,
    error: String?,
    onMyCourseClick: (String) -> Unit,
    onCourseClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.course_list_screen_title)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Мои курсы (горизонтальный список)
            if (myCourses.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.course_list_my_courses_label),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(myCourses) { course ->
                        Card(
                            modifier = Modifier
                                .width(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onMyCourseClick(course.id) },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Face,
                                    contentDescription = course.title,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = course.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                                Spacer(Modifier.height(4.dp))
                                Spacer(Modifier.height(6.dp))
                            }
                        }
                    }
                }
            }

            // Все курсы (вертикальный список)
            Text(
                text = stringResource(R.string.course_list_all_courses_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(allCourses) { course ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onCourseClick(course.id) },
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Face,
                                contentDescription = course.title,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = course.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = course.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                        }
                    }
                }
            }

            // Ошибка
            if (error != null) {
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            // Лоадер
            if (isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

// --- Превью ---
@Preview(showBackground = true)
@Composable
private fun CourseListScreenContentPreview() {
    CourseListScreenContent(
        myCourses = listOf(
            CourseEntity(
                id = "android",
                title = "Android",
                description = "Основы Android",
            )
        ),
        allCourses = listOf(
            CourseEntity(
                id = "compose",
                title = "Compose",
                description = "Jetpack Compose для Android",
            ),
            CourseEntity(
                id = "arch",
                title = "Архитектура",
                description = "Чистая архитектура приложений",
            )
        ),
        isLoading = false,
        error = null,
        onMyCourseClick = {},
        onCourseClick = {}
    )
}
