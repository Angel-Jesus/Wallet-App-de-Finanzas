package com.angelpr.wallet.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    fun getNotification(): Flow<Boolean>
    suspend fun updateNotification(enabled: Boolean)
}