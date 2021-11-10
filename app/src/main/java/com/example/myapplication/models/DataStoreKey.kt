package com.example.myapplication.models

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKey {
    val TOKEN_TYPE = stringPreferencesKey("token_type")
    val EXPIRED_IN = stringPreferencesKey("expires_in")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}