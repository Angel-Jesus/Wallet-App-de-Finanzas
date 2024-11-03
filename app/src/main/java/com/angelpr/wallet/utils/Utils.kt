package com.angelpr.wallet.utils

import java.time.LocalDate

fun <T> Iterable<T>.sumOfFloat(selector: (T) -> Float):Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun getMonthInCase(date: Long, isNextMonth: Boolean): Long {
    return if (isNextMonth) LocalDate.ofEpochDay(date).plusMonths(1)
        .toEpochDay() else date
}

object Constants{
    const val MY_CHANNEL_ID = "myChannel"
    const val NOTIFICATION_KEY = "notificacion"

    const val HOME = "Home"
    const val DEBT = "Deudas"
    const val STATISTICS = "Estadistica"
    const val SETTINGS = "Configuracion"

}