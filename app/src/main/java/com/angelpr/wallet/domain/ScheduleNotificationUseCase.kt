package com.angelpr.wallet.domain

import com.angelpr.wallet.data.NotificationRepository
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    fun schedule(notificationId: Int, year: Int, month: Int, day: Int) =
        repository.schedule(
            notificationId,
            year,
            month,
            day
        )

    fun cancel(notificationId: Int) = repository.cancel(notificationId)

}