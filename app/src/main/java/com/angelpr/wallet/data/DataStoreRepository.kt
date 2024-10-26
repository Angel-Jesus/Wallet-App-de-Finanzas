package com.angelpr.wallet.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.angelpr.wallet.utils.Constants
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository@Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun getNotification() = dataStore.data.map {preferences ->
        preferences[booleanPreferencesKey(Constants.NOTIFICATION_KEY)] ?: true
    }

    suspend fun updateNotification(enabled: Boolean){
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.NOTIFICATION_KEY)] = enabled
        }
    }

}