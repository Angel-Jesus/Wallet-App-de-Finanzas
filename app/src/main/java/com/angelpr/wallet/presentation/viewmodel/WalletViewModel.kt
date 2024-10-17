package com.angelpr.wallet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.domain.DeleteWalletUseCase
import com.angelpr.wallet.domain.GetWalletUseCase
import com.angelpr.wallet.domain.SendWalletUseCase
import com.angelpr.wallet.domain.UpdateWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletUseCase: GetWalletUseCase,
    private val sendWalletUseCase: SendWalletUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase
) : ViewModel() {

    private val _stateCard = MutableStateFlow(UiStateCard())
    val stateCard = _stateCard.asStateFlow()

    // Action by Cards
    fun getAllCard(){
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            val result = getWalletUseCase.AllCard()
            _stateCard.update { it.copy(state = ActionProcess.SUCCESS, cardList = result) }
        }
    }

    fun addNewCard(cardModel: CardModel){
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING)}
            _stateCard.update { it.copy(state = sendWalletUseCase.Card(cardModel)) }
        }
    }

    fun updateCard(cardModel: CardModel){
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING)}
            _stateCard.update { it.copy(state = updateWalletUseCase.Card(cardModel)) }
        }
    }

    fun deleteCard(id: Int){
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING)}
            _stateCard.update { it.copy(state = deleteWalletUseCase.Card(id)) }
        }
    }

    // Action by Debts

    fun updateDebtState(id: Int, isPaid: Int){
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING)}
            _stateCard.update { it.copy(state = updateWalletUseCase.DebtState(id, isPaid)) }
        }
    }

    data class UiStateCard(
        val cardList: List<CardModel> = emptyList(),
        var state: ActionProcess = ActionProcess.NOT_AVAILABLE
    )
}