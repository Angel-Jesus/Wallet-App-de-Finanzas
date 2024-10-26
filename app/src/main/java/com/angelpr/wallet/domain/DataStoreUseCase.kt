package com.angelpr.wallet.domain

import com.angelpr.wallet.data.DataStoreRepository
import javax.inject.Inject


class DataStoreUseCase @Inject constructor(
    private val repository: DataStoreRepository
){
    suspend fun updateNotification(enabled: Boolean) = repository.updateNotification(enabled)

    fun getNotification() = repository.getNotification()

}