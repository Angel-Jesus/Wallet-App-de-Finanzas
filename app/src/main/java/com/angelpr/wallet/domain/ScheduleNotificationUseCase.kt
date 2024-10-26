package com.angelpr.wallet.domain

import com.angelpr.wallet.data.NotificationRepository
import java.time.LocalDate
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
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