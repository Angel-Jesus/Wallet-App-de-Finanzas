package com.angelpr.wallet.domain

import com.angelpr.wallet.data.repository.DataStoreRepository


class DataStoreUseCase(
    private val repository: DataStoreRepository
){
    suspend fun updateNotification(enabled: Boolean) = repository.updateNotification(enabled)

    fun getNotification() = repository.getNotification()

}