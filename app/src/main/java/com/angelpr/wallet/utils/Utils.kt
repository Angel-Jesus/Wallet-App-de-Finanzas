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

fun getMonthInCase(date: Long, isNextMonth: Boolean): Long {
    return if (isNextMonth) LocalDate.ofEpochDay(date).plusMonths(1)
        .toEpochDay() else date
}

fun getDateExpired(dayExpired: Int, dateClose: Int): Long {
    val dateToday = LocalDate.now()
    val dateMonthToday = LocalDate.of(dateToday.year, dateToday.month.value, dayExpired)

    return if (dateToday.dayOfMonth > dateClose) {
        dateMonthToday.plusMonths(2).toEpochDay()
    } else {
        dateMonthToday.plusMonths(1).toEpochDay()
    }
}

fun getInitDate(date: Int): LocalDate {
    val today = LocalDate.now()
    return if(today.dayOfMonth < date) LocalDate.of(today.year, today.month.plus(-1), date) else LocalDate.of(today.year, today.month, date)
}