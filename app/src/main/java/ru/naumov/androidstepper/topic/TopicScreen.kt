// File: ru/naumov/androidstepper/topic/TopicScreen.kt
package ru.naumov.androidstepper.topic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.naumov.androidstepper.R

@Composable
fun TopicScreen(component: TopicComponent) {
    val model by component.model.subscribeAsState()
    TopicScreenContent(
        content = model.content,
        images = model.images,
        progress = model.progress,
        isLoading = model.isLoading,
        error = model.error,
        onBackClicked = component::onBackClicked,
        onTestClicked = component::onTestClicked,
        onProgressChanged = component::onProgressChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreenContent(
    content: String,
    images: List<String>,
    progress: Float,
    isLoading: Boolean,
    error: String?,
    onBackClicked: () -> Unit,
    onTestClicked: () -> Unit,
    onProgressChanged: (Float) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.topic_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.topic_nav_back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onTestClicked
            ) { Text(stringResource(R.string.topic_go_to_test)) }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val scrollState = rememberScrollState()
                // Отправляем прогресс при скролле
                LaunchedEffect(scrollState.value, scrollState.maxValue) {
                    if (scrollState.maxValue > 0) {
                        val value = scrollState.value.toFloat() / scrollState.maxValue
                        onProgressChanged(value.coerceIn(0f, 1f))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    // Контент (текст)
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                    )
                    Spacer(Modifier.height(16.dp))
                    // Картинки
                    images.forEach { imageUrl ->
                        // Пока что для моков - используем Placeholder из ресурсов (заменить если появится загрузка)
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(vertical = 8.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
            // Прогресс-бар просмотра (опционально)
            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
            // Ошибка
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}

// ---- Preview ----
@Preview(showBackground = true)
@Composable
private fun TopicScreenContentPreview() {
    TopicScreenContent(
        content = "Тут содержимое темы. Много интересного текста для изучения.",
        images = listOf("", ""),
        progress = 0.7f,
        isLoading = false,
        error = null,
        onBackClicked = {},
        onTestClicked = {},
        onProgressChanged = {}
    )
}
