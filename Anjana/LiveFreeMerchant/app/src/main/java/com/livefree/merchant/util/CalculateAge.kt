package com.livefree.merchant.util


import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by root on 28/11/19.
 */
object CalculateAge {

    fun calculateAge(birthdate: Date): Int {
        val birth = Calendar.getInstance()
        birth.setTime(birthdate)
        val today = Calendar.getInstance()
        var yearDifference = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH)) {
            yearDifference--
        } else {
            if (today.get(Calendar.MONTH) === birth.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH)) {
                yearDifference--
            }
        }
        return yearDifference
    }

    fun parseStringToDate(date: String): Date {
        val df = SimpleDateFormat("dd-MM-yyyy")
        return df.parse(date)
    }
    /*fun isValidPassword(password: String): Boolean {
       val matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{4,20})").matcher(password)
       return matcher.matches()
   }*/
}