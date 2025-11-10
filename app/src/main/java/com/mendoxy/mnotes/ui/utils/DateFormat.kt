package com.mendoxy.mnotes.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Long.format(): String{
    val date = Date(this)
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val formatted = format.format(date)
    return formatted
}

fun Long.isToday(): Boolean{
    val format = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    val today = format.format(Date())
    val noteDate = format.format(Date(this))
    return today == noteDate
}