package com.angelpr.wallet.utils

import java.time.LocalDate

fun <T> Iterable<T>.sumOfFloat(selector: (T) -> Float):Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

object Constants{
    const val MY_CHANNEL_ID = "myChannel"
    const val NOTIFICATION_KEY = "notificacion"

    const val HOME = "Home"
    const val DEBT = "Deudas"
    const val STATISTICS = "Estadistica"
    const val SETTINGS = "Configuracion"

}

fun getDateExpired(dayExpired: Int, dateClose: Int, dateToday: LocalDate): Long {
    val dateMonthToday = LocalDate.of(dateToday.year, dateToday.month.value, dayExpired)

    val date = if (dateToday.dayOfMonth > dateClose) {
        dateMonthToday.plusMonths(2)
    } else {
        dateMonthToday.plusMonths(1)
    }

    return date.toEpochDay()
}

fun getInitDate(date: Int): LocalDate {
    val today = LocalDate.now()
    return LocalDate.of(today.year, today.month, date)
}