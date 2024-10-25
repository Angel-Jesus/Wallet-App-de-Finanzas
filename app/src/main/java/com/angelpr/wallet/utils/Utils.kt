package com.angelpr.wallet.utils

fun <T> Iterable<T>.sumOfFloat(selector: (T) -> Float):Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

object Constants{
    const val MY_CHANNEL_ID = "myChannel"
}