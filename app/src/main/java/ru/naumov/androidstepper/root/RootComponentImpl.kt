package ru.naumov.androidstepper.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import ru.naumov.androidstepper.onboarding.username.UsernameComponent
import ru.naumov.androidstepper.onboarding.username.UsernameComponentImpl
import ru.naumov.androidstepper.onboarding.level.LevelComponent
import ru.naumov.androidstepper.onboarding.level.LevelComponentImpl
import ru.naumov.androidstepper.onboarding.course.CourseComponent
import ru.naumov.androidstepper.onboarding.course.CourseComponentImpl
import ru.naumov.androidstepper.home.HomeComponent
import ru.naumov.androidstepper.home.HomeComponentImpl
import ru.naumov.androidstepper.courses.CourseListComponent
import ru.naumov.androidstepper.courses.CourseListComponentImpl
import ru.naumov.androidstepper.coursedetail.CourseDetailComponent
import ru.naumov.androidstepper.coursedetail.CourseDetailComponentImpl
import ru.naumov.androidstepper.coursetopics.CourseTopicsComponent
import ru.naumov.androidstepper.coursetopics.CourseTopicsComponentImpl
import ru.naumov.androidstepper.loading.LoadingComponent
import ru.naumov.androidstepper.loading.LoadingComponent.LoadingOutput
import ru.naumov.androidstepper.loading.LoadingComponentImpl
import ru.naumov.androidstepper.topic.TopicComponent
import ru.naumov.androidstepper.topic.TopicComponentImpl
import ru.naumov.androidstepper.test.TestComponent
import ru.naumov.androidstepper.test.TestComponentImpl
import ru.naumov.androidstepper.home.HomeComponent.Output as HomeOutput
import ru.naumov.androidstepper.courses.CourseListComponent.Output as CourseListOutput
import ru.naumov.androidstepper.coursedetail.CourseDetailComponent.Output as CourseDetailOutput
import ru.naumov.androidstepper.coursetopics.CourseTopicsComponent.Output as CourseTopicsOutput
import ru.naumov.androidstepper.topic.TopicComponent.Output as TopicOutput
import ru.naumov.androidstepper.test.TestComponent.Output as TestOutput
import ru.naumov.androidstepper.onboarding.course.CourseComponent.CourseOutput
import ru.naumov.androidstepper.onboarding.level.LevelComponent.LevelOutput
import ru.naumov.androidstepper.onboarding.username.UsernameComponent.UsernameOutput
import ru.naumov.androidstepper.root.RootComponent.Child.*

class RootComponentImpl(
    componentContext: ComponentContext,
    private val usernameComponent: (ComponentContext, Consumer<UsernameOutput>) -> UsernameComponent,
    private val levelComponent: (ComponentContext, Consumer<LevelOutput>) -> LevelComponent,
    private val courseComponent: (ComponentContext, Consumer<CourseOutput>) -> CourseComponent,
    private val homeComponent: (ComponentContext, Consumer<HomeOutput>) -> HomeComponent,
    private val courseListComponent: (ComponentContext, Consumer<CourseListOutput>) -> CourseListComponent,
    private val courseDetailComponent: (ComponentContext, String, Consumer<CourseDetailOutput>) -> CourseDetailComponent,
    private val courseTopicsComponent: (ComponentContext, String, Consumer<CourseTopicsOutput>) -> CourseTopicsComponent,
    private val topicComponent: (ComponentContext, String, Consumer<TopicOutput>) -> TopicComponent,
    private val testComponent: (ComponentContext, String, Consumer<TestOutput>) -> TestComponent, // <-- добавлено
    private val loadingComponent: (ComponentContext, Consumer<LoadingComponent.LoadingOutput>) -> LoadingComponent
) : RootComponent, ComponentContext by componentContext, KoinComponent {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory
    ) : this(
        componentContext = componentContext,
        usernameComponent = { ctx, output -> UsernameComponentImpl(ctx, storeFactory, output) },
        levelComponent = { ctx, output -> LevelComponentImpl(ctx, storeFactory, output) },
        courseComponent = { ctx, output -> CourseComponentImpl(ctx, storeFactory, output) },
        homeComponent = { ctx, output -> HomeComponentImpl(ctx, storeFactory, output) },
        courseListComponent = { ctx, output -> CourseListComponentImpl(ctx, storeFactory, output) },
        courseDetailComponent = { ctx, courseId, output ->
            CourseDetailComponentImpl(
                ctx,
                storeFactory,
                output
            )
        },
        courseTopicsComponent = { ctx, courseId, output ->
            CourseTopicsComponentImpl(
                ctx,
                storeFactory,
                courseId,
                output
            )
        },
        topicComponent = { ctx, topicId, output -> TopicComponentImpl(ctx, storeFactory, output) },
        testComponent = { ctx, topicId, output ->
            TestComponentImpl(
                ctx,
                storeFactory,
                output
            )
        }, // <-- добавлено
        loadingComponent = { ctx, output -> LoadingComponentImpl(ctx, output) }
    )

    private val navigation = StackNavigation<Configuration>()

    private val stack = childStack(
        source = navigation,
        initialConfiguration = Configuration.Loading,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = Configuration.serializer()
    )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = stack

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is Configuration.Username -> Username(
                usernameComponent(componentContext, Consumer(::onUsernameOutput))
            )

            is Configuration.Level -> Level(
                levelComponent(componentContext, Consumer(::onLevelOutput))
            )

            is Configuration.Course -> Course(
                courseComponent(componentContext, Consumer(::onCourseOutput))
            )

            is Configuration.Home -> Home(
                homeComponent(componentContext, Consumer(::onHomeOutput))
            )

            is Configuration.CourseList -> CourseList(
                courseListComponent(componentContext, Consumer(::onCourseListOutput))
            )

            is Configuration.CourseDetail -> CourseDetail(
                courseDetailComponent(
                    componentContext,
                    configuration.courseId,
                    Consumer(::onCourseDetailOutput)
                )
            )

            is Configuration.CourseTopics -> CourseTopics(
                courseTopicsComponent(
                    componentContext,
                    configuration.courseId,
                    Consumer(::onCourseTopicsOutput)
                )
            )

            is Configuration.Topic -> Topic(
                topicComponent(componentContext, configuration.topicId, Consumer(::onTopicOutput))
            )

            is Configuration.Test -> Test(
                testComponent(
                    componentContext,
                    configuration.topicId,
                    Consumer(::onTestOutput)
                )
            )

            Configuration.Loading -> Loading(
                loadingComponent(componentContext, Consumer(::onLoadingOutput))
            )
        }

    // Username screen navigation
    private fun onUsernameOutput(output: UsernameOutput) {
        when (output) {
            UsernameOutput.NavigateNext -> navigation.push(Configuration.Level)
            UsernameOutput.NavigateBack -> navigation.pop()
        }
    }

    // Level screen navigation
    private fun onLevelOutput(output: LevelOutput) {
        when (output) {
            LevelOutput.NavigateNext -> navigation.push(Configuration.Course)
            LevelOutput.NavigateBack -> navigation.pop()
        }
    }

    // Onboarding course selection navigation
    private fun onCourseOutput(output: CourseOutput) {
        when (output) {
            CourseOutput.NavigateNext -> navigation.push(Configuration.Home)
            CourseOutput.NavigateBack -> navigation.pop()
        }
    }

    // Home screen navigation
    private fun onHomeOutput(output: HomeOutput) {
        when (output) {
            is HomeOutput.OpenCourse -> navigation.push(Configuration.CourseTopics(output.courseId))
            is HomeOutput.OpenRecent -> {
                navigation.push(Configuration.CourseTopics(output.courseId))
            }
        }
    }

    // Courses screen navigation
    private fun onCourseListOutput(output: CourseListOutput) {
        when (output) {
            is CourseListOutput.OpenMyCourse -> navigation.push(Configuration.CourseTopics(output.courseId))
            is CourseListOutput.OpenCourseDetail -> navigation.push(
                Configuration.CourseDetail(
                    output.courseId
                )
            )

            is CourseListOutput.ShowMessage -> { /* ничего, snackbar и т.д. */
            }
        }
    }

    // Course detail screen navigation
    private fun onCourseDetailOutput(output: CourseDetailOutput) {
        // Например, возвращаемся назад после добавления курса или Back
    }

    // Course topics screen navigation
    private fun onCourseTopicsOutput(output: CourseTopicsOutput) {
        when (output) {
            CourseTopicsOutput.Back -> navigation.pop()
            is CourseTopicsOutput.OpenTopic -> {
                navigation.push(Configuration.Topic(output.topicId))
            }
        }
    }

    // Topic screen navigation
    private fun onTopicOutput(output: TopicOutput) {
        when (output) {
            TopicOutput.NavigateBack -> navigation.pop()
            TopicOutput.NavigateToTest -> {
                navigation.push(
                    Configuration.Test(/* topicId должен быть взят из текущей конфигурации Topic */
                        lastOpenedTopicId ?: ""
                    )
                )
            }
        }
    }

    // Test screen navigation
    private fun onTestOutput(output: TestOutput) {
        when (output) {
            TestOutput.NavigateBack -> navigation.pop()
            TestOutput.ShowResult -> {
                // Можно показать что-то, например диалог
            }
        }
    }

    private fun onLoadingOutput(output: LoadingOutput) {
        when (output) {
            is LoadingOutput.onLoaded -> navigation.replaceAll(output.config)
        }
    }

    // Храним последний открытый topicId для корректного перехода к тесту
    private var lastOpenedTopicId: String? = null

    private fun openTopic(topicId: String) {
        lastOpenedTopicId = topicId
        navigation.push(Configuration.Topic(topicId))
    }

    // Course topics screen navigation с сохранением topicId
    private fun onCourseTopicsOutputV2(output: CourseTopicsOutput) {
        when (output) {
            CourseTopicsOutput.Back -> navigation.pop()
            is CourseTopicsOutput.OpenTopic -> {
                openTopic(output.topicId)
            }
        }
    }

    @Serializable
    sealed interface Configuration {
        @Serializable
        object Username : Configuration
        @Serializable
        object Level : Configuration
        @Serializable
        object Course : Configuration
        @Serializable
        object Home : Configuration
        @Serializable
        object CourseList : Configuration
        @Serializable
        data class CourseDetail(val courseId: String) : Configuration
        @Serializable
        data class CourseTopics(val courseId: String) : Configuration
        @Serializable
        data class Topic(val topicId: String) : Configuration
        @Serializable
        data class Test(val topicId: String) : Configuration    // <-- добавлено
        @Serializable
        object Loading : Configuration
    }
}
