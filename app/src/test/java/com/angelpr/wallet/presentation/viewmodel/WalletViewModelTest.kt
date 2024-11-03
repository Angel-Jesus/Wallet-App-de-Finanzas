package com.angelpr.wallet.presentation.viewmodel

import com.angelpr.wallet.domain.DataStoreUseCase
import com.angelpr.wallet.domain.DeleteWalletUseCase
import com.angelpr.wallet.domain.GetWalletUseCase
import com.angelpr.wallet.domain.ScheduleNotificationUseCase
import com.angelpr.wallet.domain.SendWalletUseCase
import com.angelpr.wallet.domain.UpdateWalletUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WalletViewModelTest{

    @RelaxedMockK
    private lateinit var getWalletUseCase: GetWalletUseCase
    @RelaxedMockK
    private lateinit var sendWalletUseCase: SendWalletUseCase
    @RelaxedMockK
    private lateinit var updateWalletUseCase: UpdateWalletUseCase
    @RelaxedMockK
    private lateinit var deleteWalletUseCase: DeleteWalletUseCase
    @RelaxedMockK
    private lateinit var notificationUseCase: ScheduleNotificationUseCase
    @RelaxedMockK
    private lateinit var dataStoreUseCase: DataStoreUseCase

    private lateinit var viewModel: WalletViewModel


    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        viewModel = WalletViewModel(
            getWalletUseCase,
            sendWalletUseCase,
            updateWalletUseCase,
            deleteWalletUseCase,
            notificationUseCase,
            dataStoreUseCase
        )

        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter(){
        Dispatchers.resetMain()
    }

}