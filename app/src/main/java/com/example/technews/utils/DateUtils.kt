package com.example.technews.utils

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat
import java.text.SimpleDateFormat


fun formatIsoDateToPattern(isoDate: String, pattern: String): String {
    val parser = ISODateTimeFormat.dateTimeParser()
    val dateTime = parser.parseDateTime(isoDate)
    val formatter = DateTimeFormat.forPattern(pattern)

    return formatter.print(dateTime)
}

fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}