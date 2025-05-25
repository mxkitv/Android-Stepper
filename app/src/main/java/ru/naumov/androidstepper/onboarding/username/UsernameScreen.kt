package ru.naumov.androidstepper.onboarding.username

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.naumov.androidstepper.R

@Composable
fun UsernameScreen(component: UsernameComponent) {
    val model by component.model.subscribeAsState()
    UsernameScreenContent(
        username = model.username,
        isLoading = model.isLoading,
        error = model.error,
        onUsernameChange = component::onUsernameChange,
        onContinue = component::onContinue,
        onBack = component::onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameScreenContent(
    username: String,
    isLoading: Boolean,
    error: String?,
    onUsernameChange: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val usernamePattern = Regex("""^[\p{L}\p{N}_\- .!@#$%^&*()+=:;'"?/<>{}\[\]|~`]{2,24}$""")
    val isValid = username.trim().matches(usernamePattern)

    val borderColor by animateColorAsState(
        when {
            isValid -> MaterialTheme.colorScheme.primary
            error != null || (username.isNotBlank() && !isValid) -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.outline
        }, label = "borderColor"
    )

    val placeholderColor by animateColorAsState(
        targetValue = when {
            isValid -> MaterialTheme.colorScheme.primary
            error != null || (username.isNotBlank() && !isValid) -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }, label = "placeholderColor"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.username_screen_title_appbar)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.username_screen_nav_back)
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
                    text = stringResource(R.string.username_screen_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.username_screen_instruction),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(28.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text(stringResource(R.string.username_field_label)) },
                    placeholder = { Text(stringResource(R.string.username_field_placeholder)) },
                    singleLine = true,
                    isError = error != null || (username.isNotBlank() && !isValid),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        focusedPlaceholderColor = placeholderColor,
                        unfocusedPlaceholderColor = placeholderColor,
                        errorPlaceholderColor = MaterialTheme.colorScheme.error
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        autoCorrectEnabled = false
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onContinue()
                            keyboardController?.hide()
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                val shouldShowError = error != null || (username.isNotBlank() && !isValid)
                if (shouldShowError) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = error ?: stringResource(R.string.username_error_default),
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
                            if (isValid && !isLoading)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            onContinue()
                            keyboardController?.hide()
                        },
                        enabled = isValid && !isLoading,
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
                                contentDescription = stringResource(R.string.username_screen_continue),
                                tint = if (isValid && !isLoading)
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


@Preview(showBackground = true)
@Composable
private fun UsernameScreenContentPreview_Default() {
    MaterialTheme {
        UsernameScreenContent(
            username = "",
            isLoading = false,
            error = null,
            onUsernameChange = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Ошибка ввода")
@Composable
private fun UsernameScreenContentPreview_Error() {
    MaterialTheme {
        UsernameScreenContent(
            username = "!",
            isLoading = false,
            error = "От 2 до 24 символов, можно буквы, цифры, спецсимволы.",
            onUsernameChange = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Валидный ввод")
@Composable
private fun UsernameScreenContentPreview_Valid() {
    MaterialTheme {
        UsernameScreenContent(
            username = "Foxy123",
            isLoading = false,
            error = null,
            onUsernameChange = {},
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Загрузка")
@Composable
private fun UsernameScreenContentPreview_Loading() {
    MaterialTheme {
        UsernameScreenContent(
            username = "robot42",
            isLoading = true,
            error = null,
            onUsernameChange = {},
            onContinue = {},
            onBack = {}
        )
    }
}
