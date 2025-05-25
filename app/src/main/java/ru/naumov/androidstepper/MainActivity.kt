package ru.naumov.androidstepper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.naumov.androidstepper.root.RootComponent
import ru.naumov.androidstepper.root.RootComponentImpl
import ru.naumov.androidstepper.root.RootScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponent = rootComponent(defaultComponentContext())

        setContent {
            RootScreen(rootComponent)
        }
    }

    private fun rootComponent(componentContext: ComponentContext): RootComponent =
        RootComponentImpl(
            componentContext = componentContext,
            storeFactory = LoggingStoreFactory(DefaultStoreFactory()),
        )
}