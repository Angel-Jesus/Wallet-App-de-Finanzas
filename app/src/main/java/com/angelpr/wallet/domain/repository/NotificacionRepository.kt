package com.angelpr.wallet.domain.repository

import java.time.LocalDate

interface NotificacionRepository {

    fun schedule(
        cardName: String,
        dateExpired: LocalDate,
        notificationId: Int,
        year: Int,
        month: Int,
        day: Int
    )

    fun cancel(notificationId: Int)
}