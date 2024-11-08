package com.angelpr.wallet.domain

import com.angelpr.wallet.data.repository.WalletRepositoryImpl
import com.angelpr.wallet.domain.use_case.wallet.GetWalletUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetWalletUseCaseTest{

    @RelaxedMockK // Mockeamos la clase
    private lateinit var repository: WalletRepositoryImpl

    private lateinit var getWalletUseCase: GetWalletUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getWalletUseCase = GetWalletUseCase(repository)
    }

    @Test
    fun `When get empty list`():Unit = runBlocking {
        // Given
        coEvery { getWalletUseCase.allCard() } returns emptyList()

        // When
        getWalletUseCase.allCard()

        // Then
        coVerify(exactly = 1) { repository.getAllCardFromDatabase() }
    }
}