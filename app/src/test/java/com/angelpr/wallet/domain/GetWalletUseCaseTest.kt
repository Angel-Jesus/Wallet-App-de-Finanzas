package com.angelpr.wallet.domain

import com.angelpr.wallet.data.WalletRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetWalletUseCaseTest{

    @RelaxedMockK // Mockeamos la clase
    private lateinit var repository: WalletRepository

    private lateinit var getWalletUseCase: GetWalletUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getWalletUseCase = GetWalletUseCase(repository)
    }

    @Test
    fun `When get empty list`():Unit = runBlocking {
        // Given
        coEvery { getWalletUseCase.AllCard() } returns emptyList()

        // When
        getWalletUseCase.AllCard()

        // Then
        coVerify(exactly = 1) { repository.getAllCardFromDatabase() }
    }
}