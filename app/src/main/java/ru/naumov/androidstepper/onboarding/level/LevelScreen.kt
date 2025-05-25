package ru.naumov.androidstepper.onboarding.level

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

@Composable
fun LevelScreen(component: LevelComponent) {
    val model by component.model.subscribeAsState()
    LevelScreenContent(
        selectedLevel = model.selectedLevel,
        isLoading = model.isLoading,
        error = model.error,
        onLevelSelected = component::onLevelSelected,
        onContinue = component::onContinue,
        onBack = component::onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreenContent(
    selectedLevel: UserLevel?,
    isLoading: Boolean,
    error: String?,
    onLevelSelected: (UserLevel) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.level_screen_title_appbar)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.level_screen_nav_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .imePadding()
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
                    text = stringResource(R.string.level_screen_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.level_screen_instruction),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(28.dp))
                LevelOptions(
                    selectedLevel = selectedLevel,
                    onLevelSelected = onLevelSelected,
                    enabled = !isLoading
                )
                val shouldShowError = error != null
                if (shouldShowError) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (error == "level_error_choose_level")
                            stringResource(R.string.level_error_choose_level)
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
                            if (selectedLevel != null && !isLoading)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onContinue,
                        enabled = selectedLevel != null && !isLoading,
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
                                contentDescription = stringResource(R.string.level_screen_continue),
                                tint = if (selectedLevel != null && !isLoading)
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
private fun LevelOptions(
    selectedLevel: UserLevel?,
    onLevelSelected: (UserLevel) -> Unit,
    enabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserLevel.values().forEach { level ->
            val isSelected = selectedLevel == level
            val colors = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(colors)
                    .clickable(enabled = enabled) { onLevelSelected(level) },
                tonalElevation = if (isSelected) 4.dp else 0.dp,
                shadowElevation = if (isSelected) 8.dp else 0.dp,
            ) {
                Text(
                    text = when (level) {
                        UserLevel.BEGINNER -> stringResource(R.string.level_beginner)
                        UserLevel.INTERMEDIATE -> stringResource(R.string.level_intermediate)
                        UserLevel.ADVANCED -> stringResource(R.string.level_advanced)
                    },
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

@Preview(showBackground = true)
@Composable
private fun LevelScreenContentPreview_Default() {
    MaterialTheme {
        LevelScreenContent(
            selectedLevel = null,
            isLoading = false,
            error = null,
            onLevelSelected = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Выбран уровень")
@Composable
private fun LevelScreenContentPreview_Selected() {
    MaterialTheme {
        LevelScreenContent(
            selectedLevel = UserLevel.INTERMEDIATE,
            isLoading = false,
            error = null,
            onLevelSelected = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Ошибка")
@Composable
private fun LevelScreenContentPreview_Error() {
    MaterialTheme {
        LevelScreenContent(
            selectedLevel = null,
            isLoading = false,
            error = "Пожалуйста, выберите уровень",
            onLevelSelected = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Загрузка")
@Composable
private fun LevelScreenContentPreview_Loading() {
    MaterialTheme {
        LevelScreenContent(
            selectedLevel = UserLevel.BEGINNER,
            isLoading = true,
            error = null,
            onLevelSelected = {},
            onContinue = {},
            onBack = {}
        )
    }
}
