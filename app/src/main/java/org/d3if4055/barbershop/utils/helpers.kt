package org.d3if4055.barbershop.utils

import java.text.NumberFormat
import java.util.*


fun rupiah(uang: Double): String{
    val localeID =  Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(uang).toString()
}