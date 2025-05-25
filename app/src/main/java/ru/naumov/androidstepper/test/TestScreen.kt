package ru.naumov.androidstepper.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
        onBack = component::onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestScreenContent(
    model: TestComponent.TestModel,
    onAnswerSelected: (String, Int) -> Unit,
    onSubmit: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            if (model.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (model.showResult) {
                Text("Результат: ${model.correctCount} из ${model.questions.size}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("Попробовать снова")
                }
            } else {
                model.questions.forEachIndexed { index, question ->
                    QuestionItem(
                        question = question,
                        selectedAnswer = model.answers[question.id],
                        onSelect = { answerIdx ->
                            onAnswerSelected(question.id, answerIdx)
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                if (model.error != null) {
                    Text(model.error!!, color = MaterialTheme.colorScheme.error)
                }
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Проверить")
                }
            }
        }
    }
}

@Composable
private fun QuestionItem(
    question: Question,
    selectedAnswer: Int?,
    onSelect: (Int) -> Unit
) {
    Text(question.text, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    question.answers.forEachIndexed { idx, answer ->
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

@Preview(showBackground = true)
@Composable
fun TestScreenContentPreview() {
    val questions = listOf(
        Question(
            id = "q1",
            text = "Что такое Android?",
            answers = listOf("ОС", "Язык программирования", "Фреймворк"),
            correctAnswer = 0
        ),
        Question(
            id = "q2",
            text = "Кто разрабатывает Jetpack Compose?",
            answers = listOf("Apple", "Google", "Microsoft"),
            correctAnswer = 1
        )
    )
    val model = TestComponent.TestModel(
        questions = questions,
        answers = mapOf("q1" to 0),
        isLoading = false,
        error = null,
        showResult = false,
        correctCount = 0
    )
    TestScreenContent(
        model = model,
        onAnswerSelected = { _, _ -> },
        onSubmit = {},
        onRetry = {},
        onBack = {}
    )
}
