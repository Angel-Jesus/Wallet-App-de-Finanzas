package com.angelpr.wallet.domain.use_case

import com.angelpr.wallet.domain.repository.DataStoreRepository


class DataStoreUseCase(
    private val repository: DataStoreRepository
){
    suspend fun updateNotification(enabled: Boolean) = repository.updateNotification(enabled)

    fun getNotification() = repository.getNotification()

}