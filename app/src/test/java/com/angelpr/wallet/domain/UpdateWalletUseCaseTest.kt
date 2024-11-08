package com.angelpr.wallet.domain

import com.angelpr.wallet.data.repository.WalletRepositoryImpl
import com.angelpr.wallet.domain.use_case.wallet.UpdateWalletUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDate


class UpdateWalletUseCaseTest{

    @RelaxedMockK
    private lateinit var repository : WalletRepositoryImpl

    private lateinit var updateWalletUseCase : UpdateWalletUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        updateWalletUseCase = UpdateWalletUseCase(repository)
    }

    @Test
    fun `When quotas is equal to quotasPaid`():Unit = runBlocking {
        // Given
        val dateExpired = LocalDate.of(2024, 11, 5).toEpochDay()

        // When
        val response = updateWalletUseCase.debtState(1, 1, 1, dateExpired)
        println(response.name)
        // Then
        coVerify(exactly = 1) { repository.updateDebtToDatabase(any(), any(), any(), any()) }
    }
}