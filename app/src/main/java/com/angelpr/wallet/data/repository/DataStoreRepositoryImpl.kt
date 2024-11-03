package com.angelpr.wallet.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.angelpr.wallet.domain.repository.DataStoreRepository
import com.angelpr.wallet.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): DataStoreRepository {

    override fun getNotification(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(Constants.NOTIFICATION_KEY)] ?: true
    }

    override suspend fun updateNotification(enabled: Boolean){
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.NOTIFICATION_KEY)] = enabled
        }
    }

}