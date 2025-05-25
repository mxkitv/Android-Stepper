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
import ru.naumov.androidstepper.data.CourseRepository
import ru.naumov.androidstepper.data.SelectedCourseRepository
import ru.naumov.androidstepper.data.database.AppDatabase
import ru.naumov.androidstepper.onboarding.course.CourseComponent
import ru.naumov.androidstepper.onboarding.course.CourseComponentImpl
import ru.naumov.androidstepper.onboarding.level.LevelComponent
import ru.naumov.androidstepper.onboarding.level.LevelComponentImpl
import ru.naumov.androidstepper.onboarding.level.LevelStoreFactory
import ru.naumov.androidstepper.onboarding.username.UsernameComponent

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

}
