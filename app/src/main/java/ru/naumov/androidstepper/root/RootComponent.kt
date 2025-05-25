package ru.naumov.androidstepper.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.naumov.androidstepper.onboarding.username.UsernameComponent
import ru.naumov.androidstepper.onboarding.level.LevelComponent
import ru.naumov.androidstepper.onboarding.course.CourseComponent
import ru.naumov.androidstepper.home.HomeComponent
import ru.naumov.androidstepper.courses.CourseListComponent
import ru.naumov.androidstepper.coursedetail.CourseDetailComponent
import ru.naumov.androidstepper.coursetopics.CourseTopicsComponent
import ru.naumov.androidstepper.topic.TopicComponent
import ru.naumov.androidstepper.test.TestComponent

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Username(val component: UsernameComponent) : Child
        data class Level(val component: LevelComponent) : Child
        data class Course(val component: CourseComponent) : Child
        data class Home(val component: HomeComponent) : Child
        data class CourseList(val component: CourseListComponent) : Child
        data class CourseDetail(val component: CourseDetailComponent) : Child
        data class CourseTopics(val component: CourseTopicsComponent) : Child
        data class Topic(val component: TopicComponent) : Child
        data class Test(val component: TestComponent) : Child        // <--- добавлено
    }
}
