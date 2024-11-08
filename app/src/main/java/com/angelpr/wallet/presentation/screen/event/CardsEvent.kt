package com.angelpr.wallet.presentation.screen.event

import com.angelpr.wallet.data.model.CardModel

sealed class CardsEvent {
    data object GetCards: CardsEvent()
    data class AddCard(val card:CardModel): CardsEvent()
    data class DeleteCard(val card:CardModel): CardsEvent()
    data class UpdateCard(val card:CardModel): CardsEvent()
    data class CardSelected(val card:CardModel): CardsEvent()
}