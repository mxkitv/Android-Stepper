package ru.naumov.androidstepper.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.naumov.androidstepper.data.UserRepository
import ru.naumov.androidstepper.onboarding.username.UsernameComponentImpl
import ru.naumov.androidstepper.onboarding.username.UsernameStoreFactory
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.base.Consumer
import ru.naumov.androidstepper.coursetopics.CourseTopicsComponent
import ru.naumov.androidstepper.coursetopics.CourseTopicsComponentImpl
import ru.naumov.androidstepper.data.CourseRepository
import ru.naumov.androidstepper.data.MaterialRepository
import ru.naumov.androidstepper.data.SelectedCourseRepository
import ru.naumov.androidstepper.data.TestRepository
import ru.naumov.androidstepper.data.TopicRepository
import ru.naumov.androidstepper.data.database.AppDatabase
import ru.naumov.androidstepper.onboarding.course.CourseComponent
import ru.naumov.androidstepper.onboarding.course.CourseComponentImpl
import ru.naumov.androidstepper.onboarding.level.LevelComponent
import ru.naumov.androidstepper.onboarding.level.LevelComponentImpl
import ru.naumov.androidstepper.onboarding.level.LevelStoreFactory
import ru.naumov.androidstepper.onboarding.username.UsernameComponent
import ru.naumov.androidstepper.test.TestComponent
import ru.naumov.androidstepper.test.TestComponentImpl
import ru.naumov.androidstepper.topic.TopicComponent
import ru.naumov.androidstepper.topic.TopicComponentImpl

val appModule = module {
    single { UserRepository(androidContext()) }
    single<StoreFactory> { LoggingStoreFactory(DefaultStoreFactory()) }

    factory { UsernameStoreFactory(get(), get()) }

    factory { (output: (UsernameComponent.UsernameOutput) -> Unit, context: ComponentContext) ->
        UsernameComponentImpl(
            componentContext = context,
            storeFactory = get(),
            output = output
        )
    }

    factory { LevelStoreFactory(get(), get()) }
    factory { (output: (LevelComponent.LevelOutput) -> Unit, context: ComponentContext) ->
        LevelComponentImpl(
            componentContext = context,
            storeFactory = get(),
            output = output
        )
    }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "android_stepper_db"
        ).build()
    }

    single { get<AppDatabase>().selectedCourseDao() }
    single { get<AppDatabase>().courseDao() }
    single { get<AppDatabase>().topicDao() }
    single { get<AppDatabase>().materialDao() }
    single { get<AppDatabase>().testDao() }


    factory { SelectedCourseRepository(get()) }

    factory { CourseRepository(get()) }

    factory { CourseRepository(get()) }
    factory { SelectedCourseRepository(get()) } // если dao, dao
    factory { (output: (CourseComponent.CourseOutput) -> Unit, context: ComponentContext) ->
        CourseComponentImpl(
            componentContext = context,
            storeFactory = get(),
            output = output
        )
    }

    single { TopicRepository(get()) }
    factory { (output: (CourseTopicsComponent.Output) -> Unit, context: ComponentContext, courseId: String) ->
        CourseTopicsComponentImpl(
            componentContext = context,
            storeFactory = get(),
            courseId = courseId,
            output = output
        )
    }

    single { MaterialRepository(get()) }
    factory { (output: (TopicComponent.Output) -> Unit, context: ComponentContext, topicId: String) ->
        TopicComponentImpl(
            componentContext = context,
            storeFactory = get(),
            topicId = topicId,
            output = output
        )
    }

    factory { TestRepository(get()) }
    factory { (output: (TestComponent.Output) -> Unit, context: ComponentContext, topicId: String) ->
        TestComponentImpl(
            componentContext = context,
            storeFactory = get(),
            topicId = topicId,
            output = output
        )
    }

}
