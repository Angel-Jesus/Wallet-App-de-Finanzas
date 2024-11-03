package com.angelpr.wallet.domain.use_case

import com.angelpr.wallet.domain.repository.NotificacionRepository
import java.time.LocalDate

class NotificationUseCase(
    private val repository: NotificacionRepository
) {
    fun schedule(cardName: String, dateExpired: LocalDate, notificationId: Int, year: Int, month: Int, day: Int) =
        repository.schedule(
            cardName = cardName,
            dateExpired = dateExpired,
            notificationId = notificationId,
            year = year,
            month = month,
            day = day
        )

    fun cancel(notificationId: Int) = repository.cancel(notificationId)

}