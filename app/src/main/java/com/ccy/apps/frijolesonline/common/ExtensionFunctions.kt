package com.ccy.apps.frijolesonline.presentation.common_components

import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateString():String{
    return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(this)
}

fun Date.toDateTimeString():String{
    return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).format(this)
}

fun Date.toReadableDateString():String{
    return SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(this)
}

fun String.toDate(): Date?{
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(this)
    }catch (e:Exception){
        null
    }
}

fun String.removeNonSpacingMarks() =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")

fun String.getMunicipality():String{
    return this.substringAfter(" - ")
}

fun String.capitalizeFirstLetter(): String {
    if (isEmpty()) {
        return this
    }

    val firstChar = this[0].uppercase()
    val remainingChars = substring(1)

    return "$firstChar$remainingChars"
}