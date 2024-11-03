package com.angelpr.wallet.presentation.viewmodel

import com.angelpr.wallet.domain.use_case.DataStoreUseCase
import com.angelpr.wallet.domain.use_case.wallet.DeleteWalletUseCase
import com.angelpr.wallet.domain.use_case.wallet.GetWalletUseCase
import com.angelpr.wallet.domain.use_case.NotificationUseCase
import com.angelpr.wallet.domain.use_case.wallet.AddWalletUseCase
import com.angelpr.wallet.domain.use_case.wallet.UpdateWalletUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class WalletViewModelTest{

    @RelaxedMockK
    private lateinit var getWalletUseCase: GetWalletUseCase
    @RelaxedMockK
    private lateinit var addWalletUseCase: AddWalletUseCase
    @RelaxedMockK
    private lateinit var updateWalletUseCase: UpdateWalletUseCase
    @RelaxedMockK
    private lateinit var deleteWalletUseCase: DeleteWalletUseCase
    @RelaxedMockK
    private lateinit var notificationUseCase: NotificationUseCase
    @RelaxedMockK
    private lateinit var dataStoreUseCase: DataStoreUseCase

    private lateinit var viewModel: WalletViewModel


    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        viewModel = WalletViewModel(
            getWalletUseCase,
            addWalletUseCase,
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