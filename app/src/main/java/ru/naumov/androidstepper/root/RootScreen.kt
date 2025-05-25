// File: ru/naumov/androidstepper/root/RootScreen.kt
package ru.naumov.androidstepper.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.naumov.androidstepper.onboarding.username.UsernameScreen
import ru.naumov.androidstepper.onboarding.level.LevelScreen
import ru.naumov.androidstepper.onboarding.course.CourseScreen
import ru.naumov.androidstepper.home.HomeScreen
import ru.naumov.androidstepper.courses.CourseListScreen
import ru.naumov.androidstepper.coursedetail.CourseDetailScreen
import ru.naumov.androidstepper.coursetopics.CourseTopicsScreen
import ru.naumov.androidstepper.topic.TopicScreen
import ru.naumov.androidstepper.test.TestScreen // <--- добавлено

@Composable
fun RootScreen(component: RootComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Username -> UsernameScreen(child.component)
            is RootComponent.Child.Level -> LevelScreen(child.component)
            is RootComponent.Child.Course -> CourseScreen(child.component)
            is RootComponent.Child.Home -> HomeScreen(child.component)
            is RootComponent.Child.CourseList -> CourseListScreen(child.component)
            is RootComponent.Child.CourseDetail -> CourseDetailScreen(child.component)
            is RootComponent.Child.CourseTopics -> CourseTopicsScreen(child.component)
            is RootComponent.Child.Topic -> TopicScreen(child.component)
            is RootComponent.Child.Test -> TestScreen(child.component)           // <--- добавлено
        }
    }
}
