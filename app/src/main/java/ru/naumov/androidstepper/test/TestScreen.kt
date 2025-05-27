package ru.naumov.androidstepper.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun TestScreen(component: TestComponent) {
    val model by component.model.subscribeAsState()

    TestScreenContent(
        model = model,
        onAnswerSelected = component::onAnswerSelected,
        onSubmit = component::onSubmit,
        onRetry = component::onRetry,
        onBack = component::onBack,
        onContinue = component::onContinue
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestScreenContent(
    model: TestComponent.TestModel,
    onAnswerSelected: (String, Int) -> Unit,
    onSubmit: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тест") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                model.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                model.showResult -> {
                    // Все ответы верные?
                    val allCorrect = model.correctCount == model.questions.size && model.questions.isNotEmpty()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (allCorrect) {
                            Text(
                                text = "Поздравляем, вы ответили правильно на все вопросы!",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = onContinue) {
                                Text("Продолжить")
                            }
                        } else {
                            Text("Результат: ${model.correctCount} из ${model.questions.size}")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onRetry) {
                                Text("Попробовать снова")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        itemsIndexed(model.questions) { _, question ->
                            QuestionItem(
                                question = question,
                                selectedAnswer = model.answers[question.id],
                                onSelect = { answerIdx ->
                                    onAnswerSelected(question.id, answerIdx)
                                }
                            )
                        }
                        item {
                            if (model.error != null) {
                                Text(model.error!!, color = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Button(
                                onClick = onSubmit,
                                modifier = Modifier.align(Alignment.BottomCenter)
                            ) {
                                Text("Проверить")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionItem(
    question: ru.naumov.androidstepper.data.database.Question,
    selectedAnswer: Int?,
    onSelect: (Int) -> Unit
) {
    Column {
        Text(question.text, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        question.answers.withIndex().forEach { (idx, answer) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedAnswer == idx,
                        onClick = { onSelect(idx) }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnswer == idx,
                    onClick = { onSelect(idx) }
                )
                Text(answer, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestScreenContentPreview() {
    val questions = listOf(
        ru.naumov.androidstepper.data.database.Question(
            id = "q1",
            text = "Что такое Android?",
            answers = listOf("ОС", "Язык программирования", "Фреймворк"),
            correctAnswer = 0
        ),
        ru.naumov.androidstepper.data.database.Question(
            id = "q2",
            text = "Кто разрабатывает Jetpack Compose?",
            answers = listOf("Apple", "Google", "Microsoft"),
            correctAnswer = 1
        )
    )
    val model = TestComponent.TestModel(
        questions = questions,
        answers = mapOf("q1" to 0, "q2" to 1),
        isLoading = false,
        error = null,
        showResult = true,
        correctCount = 2
    )
    TestScreenContent(
        model = model,
        onAnswerSelected = { _, _ -> },
        onSubmit = {},
        onRetry = {},
        onBack = {},
        onContinue = {},
    )
}
