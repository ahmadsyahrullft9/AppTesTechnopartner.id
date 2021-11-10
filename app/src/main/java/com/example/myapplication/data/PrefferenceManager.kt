package com.example.myapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.models.DataStoreKey
import com.example.myapplication.models.UserAccess
import kotlinx.coroutines.flow.mapLatest

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class PrefferenceManager(val context: Context) {


    val userAccessFlow = context.dataStore.data.mapLatest { pref ->

        val tokenType =
            if (pref[DataStoreKey.TOKEN_TYPE] != null) pref[DataStoreKey.TOKEN_TYPE].toString() else ""
        val expiredIn =
            if (pref[DataStoreKey.EXPIRED_IN] != null) pref[DataStoreKey.EXPIRED_IN]!!.toInt() else 0
        val accessToken =
            if (pref[DataStoreKey.ACCESS_TOKEN] != null) pref[DataStoreKey.ACCESS_TOKEN].toString() else ""
        val refreshToken =
            if (pref[DataStoreKey.REFRESH_TOKEN] != null) pref[DataStoreKey.REFRESH_TOKEN].toString() else ""

        UserAccess(tokenType, expiredIn, accessToken, refreshToken)
    }

    suspend fun updateUserAccess(userAccess: UserAccess) {
        context.dataStore.edit { pref ->
            pref[DataStoreKey.TOKEN_TYPE] = userAccess.tokenType
            pref[DataStoreKey.EXPIRED_IN] = userAccess.expiresIn.toString()
            pref[DataStoreKey.ACCESS_TOKEN] = userAccess.accessToken
            pref[DataStoreKey.REFRESH_TOKEN] = userAccess.refreshToken
        }
    }

    suspend fun resetUserAccess() {
        context.dataStore.edit { pref ->
            pref.clear()
        }
    }
}