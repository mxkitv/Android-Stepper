package ru.naumov.androidstepper.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserRepository(private val context: Context) {
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val USER_LEVEL_KEY = stringPreferencesKey("user_level")
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
        }
    }

    suspend fun getUsername(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[USERNAME_KEY]
    }

    suspend fun saveUserLevel(level: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_LEVEL_KEY] = level
        }
    }

    suspend fun getUserLevel(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[USER_LEVEL_KEY]
    }
}
