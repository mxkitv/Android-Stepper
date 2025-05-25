package ru.naumov.androidstepper.loading

import com.arkivanov.decompose.ComponentContext
import com.badoo.reaktive.base.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.naumov.androidstepper.data.SelectedCourseRepository
import ru.naumov.androidstepper.data.UserRepository
import ru.naumov.androidstepper.root.RootComponentImpl
import kotlin.getValue

class LoadingComponentImpl(
    componentContext: ComponentContext,
    private val output: Consumer<LoadingComponent.LoadingOutput>
) : LoadingComponent, ComponentContext by componentContext, KoinComponent {

    private val userRepository: UserRepository by inject()
    private val selectedCourseRepository: SelectedCourseRepository by inject()

    init {
        // Сразу после создания — запускаем корутину
        CoroutineScope(Dispatchers.Main).launch {
            val username = userRepository.getUsername()
            val userLevel = userRepository.getUserLevel()
            val selectedCourses = selectedCourseRepository.getSelectedCourses()

            val nextScreen = when {
                username.isNullOrBlank() -> RootComponentImpl.Configuration.Username
                userLevel.isNullOrBlank() -> RootComponentImpl.Configuration.Level
                selectedCourses.isEmpty() -> RootComponentImpl.Configuration.Course
                else -> RootComponentImpl.Configuration.Home
            }

            output.onNext(LoadingComponent.LoadingOutput.onLoaded(nextScreen))
        }
    }
}
