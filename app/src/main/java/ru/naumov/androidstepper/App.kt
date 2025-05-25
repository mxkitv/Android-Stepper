package ru.naumov.androidstepper

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.naumov.androidstepper.data.database.AppDatabase
import ru.naumov.androidstepper.data.database.CourseEntity
import ru.naumov.androidstepper.data.database.MaterialEntity
import ru.naumov.androidstepper.data.database.TopicEntity
import ru.naumov.androidstepper.di.appModule
import kotlin.getValue

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        val db: AppDatabase by inject()

        val coursesJson = assets.open("courses.json").bufferedReader().readText()
        val topicsJson = assets.open("topics.json").bufferedReader().readText()
        val materialsJson = assets.open("materials.json").bufferedReader().readText()

        val courses = Json.decodeFromString<List<CourseEntity>>(coursesJson)
        val topics = Json.decodeFromString<List<TopicEntity>>(topicsJson)
        val materials = Json.decodeFromString<List<MaterialEntity>>(materialsJson)

        CoroutineScope(Job()).launch {
            db.courseDao().insertAll(courses)
            db.topicDao().insertAll(topics)
            db.materialDao().insertAll(materials)
        }
    }
}
