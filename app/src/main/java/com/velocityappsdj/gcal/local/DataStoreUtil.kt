package com.velocityappsdj.gcal.local

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreUtil(context: Context) {

    private val TOKEN = stringPreferencesKey("token")
    private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    private val Context.dataStore by preferencesDataStore("auth")
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN] = token
        }

    }

    fun getToken(): Flow<String?> {
        val token = dataStore.data.map {
            it[TOKEN]
        }

        return token
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit {
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    fun getRefreshToken(): Flow<String?> {
        val token = dataStore.data.map {
            it[REFRESH_TOKEN]
        }

       return token
    }
}