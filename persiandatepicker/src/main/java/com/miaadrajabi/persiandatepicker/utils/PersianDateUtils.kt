package com.miaadrajabi.persiandatepicker.utils

import java.util.Calendar
import java.util.Date

/**
 * Persian Calendar Date Utility Class
 * Utilities for converting Gregorian to Jalali and working with Persian calendar
 */
data class PersianDateUtils(
    val year: Int,
    val month: Int, // 1-12
    val day: Int
) {
    companion object {
        // Persian month names
        val PERSIAN_MONTHS = arrayOf(
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
        )
        
        // Persian weekdays
        val PERSIAN_WEEKDAYS = arrayOf(
            "شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه"
        )
        
        // Abbreviated weekdays
        val PERSIAN_WEEKDAYS_SHORT = arrayOf(
            "ش", "ی", "د", "س", "چ", "پ", "ج"
        )
        
        // Convert English digits to Persian digits
        fun toPersianNumber(number: String): String {
            val persianDigits = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
            var result = number
            for (i in 0..9) {
                result = result.replace(i.toString(), persianDigits[i])
            }
            return result
        }
        
        fun toPersianNumber(number: Int): String {
            return toPersianNumber(number.toString())
        }
        
        // Convert Jalali date to Gregorian
        fun fromPersianDate(persianDate: PersianDateUtils): Date {
            val calendar = Calendar.getInstance()
            val gregorianDate = jalaliToGregorian(persianDate.year, persianDate.month, persianDate.day)
            calendar.set(gregorianDate[0], gregorianDate[1] - 1, gregorianDate[2])
            return calendar.time
        }
        
        // Convert Gregorian date to Jalali
        fun toPersianDate(date: Date): PersianDate {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val jalaliDate = gregorianToJalali(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            return PersianDate(jalaliDate[0], jalaliDate[1], jalaliDate[2])
        }
        
        // Check Jalali leap year
        fun isLeapYear(year: Int): Boolean {
            // 33-year cycle for Jalali calendar
            val cycle = year % 33
            
            // Leap years in the 33-year cycle
            val leapYears = intArrayOf(1, 5, 9, 13, 17, 22, 26, 30)
            
            return leapYears.contains(cycle)
        }
        
        // Number of days in each Jalali month
        fun getDaysInMonth(year: Int, month: Int): Int {
            return when (month) {
                in 1..6 -> 31
                in 7..11 -> 30
                12 -> if (isLeapYear(year)) 30 else 29
                else -> 0
            }
        }
        
        // Convert Jalali date to Gregorian
        private fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): IntArray {
            // Calculate Gregorian year
            val gy = jy + 621
            
            // Days from start of Jalali year
            val daysFromJalaliNewYear = if (jm <= 6) {
                (jm - 1) * 31 + jd
            } else {
                186 + (jm - 7) * 30 + jd
            }
            
            // Nowruz (21 March)
            val march21 = 80 // day 80 of the year
            
            // Day of year in Gregorian
            val dayOfYear = march21 + daysFromJalaliNewYear - 1
            
            // Check Gregorian leap year
            val isLeap = ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))
            val daysInYear = if (isLeap) 366 else 365
            
            val monthDays = if (isLeap) 
                intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31) else
                intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            
            if (dayOfYear <= daysInYear) {
                // Same Gregorian year
                var gm = 1
                var remainingDays = dayOfYear
                
                for (i in 0..11) {
                    if (remainingDays <= monthDays[i]) {
                        gm = i + 1
                        break
                    }
                    remainingDays -= monthDays[i]
                }
                
                return intArrayOf(gy, gm, remainingDays)
            } else {
                // Next Gregorian year
                val nextYear = gy + 1
                val isNextLeap = ((nextYear % 4 == 0 && nextYear % 100 != 0) || (nextYear % 400 == 0))
                val nextMonthDays = if (isNextLeap) 
                    intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31) else
                    intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
                
                var gm = 1
                var remainingDays = dayOfYear - daysInYear
                
                for (i in 0..11) {
                    if (remainingDays <= nextMonthDays[i]) {
                        gm = i + 1
                        break
                    }
                    remainingDays -= nextMonthDays[i]
                }
                
                return intArrayOf(nextYear, gm, remainingDays)
            }
        }
        
        // Convert Gregorian to Jalali - precise algorithm
        private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): IntArray {
            val monthDays = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            val isLeap = ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))
            if (isLeap) monthDays[1] = 29
            
            var dayOfYear = gd
            for (i in 0 until gm - 1) {
                dayOfYear += monthDays[i]
            }
            
            // Calculate Jalali year
            val jy = gy - 621
            
            // Nowruz for this year
            val march21 = 80 // day 80 (21 March)
            
            // If before Nowruz
            if (dayOfYear < march21) {
                val prevYear = jy - 1
                val daysFromPrevNewYear = dayOfYear + (if (isLeap) 366 else 365) - march21
                
                // Calculate month and day
                if (daysFromPrevNewYear < 186) {
                    val jm = daysFromPrevNewYear / 31 + 1
                    val jd = daysFromPrevNewYear % 31 + 1
                    return intArrayOf(prevYear, jm, jd)
                } else {
                    val daysAfterSixMonths = daysFromPrevNewYear - 186
                    val jm = daysAfterSixMonths / 30 + 7
                    val jd = daysAfterSixMonths % 30 + 1
                    return intArrayOf(prevYear, jm, jd)
                }
            } else {
                // After Nowruz
                val daysFromNewYear = dayOfYear - march21
                
                if (daysFromNewYear < 186) {
                    val jm = daysFromNewYear / 31 + 1
                    val jd = daysFromNewYear % 31 + 1
                    return intArrayOf(jy, jm, jd)
                } else {
                    val daysAfterSixMonths = daysFromNewYear - 186
                    val jm = daysAfterSixMonths / 30 + 7
                    val jd = daysAfterSixMonths % 30 + 1
                    return intArrayOf(jy, jm, jd)
                }
            }
        }
    }
    
    // Utility methods for Jalali date
    fun getMonthName(): String = PERSIAN_MONTHS[month - 1]
    
    fun getFormattedDate(): String {
        val dayStr = toPersianNumber(day)
        val monthStr = getMonthName()
        val yearStr = toPersianNumber(year)
        return "$dayStr $monthStr $yearStr"
    }
    
    fun getShortFormattedDate(): String {
        return "${toPersianNumber(year)}/${toPersianNumber(month.toString().padStart(2, '0'))}/${toPersianNumber(day.toString().padStart(2, '0'))}"
    }
    
    fun getNumericFormattedDate(): String {
        return "${toPersianNumber(year)}/${toPersianNumber(month.toString().padStart(2, '0'))}/${toPersianNumber(day.toString().padStart(2, '0'))}"
    }
    
    fun getFullFormattedDate(): String {
        return "${getFormattedDate()}\n${getNumericFormattedDate()}"
    }
    
    fun addDays(days: Int): PersianDate {
        val gregorianDate = fromPersianDate(this)
        val calendar = Calendar.getInstance()
        calendar.time = gregorianDate
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return toPersianDate(calendar.time)
    }
    
    fun addMonths(months: Int): PersianDate {
        var newYear = year
        var newMonth = month + months
        
        while (newMonth > 12) {
            newMonth -= 12
            newYear += 1
        }
        
        while (newMonth < 1) {
            newMonth += 12
            newYear -= 1
        }
        
        val maxDay = getDaysInMonth(newYear, newMonth)
        val newDay = if (day > maxDay) maxDay else day
        
        return PersianDate(newYear, newMonth, newDay)
    }
    
    fun isSameDay(other: PersianDate): Boolean {
        return year == other.year && month == other.month && day == other.day
    }
    
    fun isAfter(other: PersianDate): Boolean {
        return when {
            year > other.year -> true
            year < other.year -> false
            month > other.month -> true
            month < other.month -> false
            else -> day > other.day
        }
    }
    
    fun isBefore(other: PersianDate): Boolean {
        return when {
            year < other.year -> true
            year > other.year -> false
            month < other.month -> true
            month > other.month -> false
            else -> day < other.day
        }
    }
    
    fun getDayOfWeek(): Int {
        val gregorianDate = fromPersianDate(this)
        val calendar = Calendar.getInstance()
        calendar.time = gregorianDate
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        
        // Convert to Persian system (Saturday = 0)
        dayOfWeek = (dayOfWeek + 1) % 7
        
        return dayOfWeek
    }
    
    fun getDayOfWeekName(): String {
        return PERSIAN_WEEKDAYS[getDayOfWeek()]
    }
}

/**
 * Persian Calendar Helper Class
 * کلاس کمکی برای کار با تقویم فارسی
 */
class PersianCalendar {
    private var persianDate: PersianDate
    
    constructor() {
        persianDate = PersianDate.toPersianDate(Date())
    }
    
    constructor(date: Date) {
        persianDate = PersianDate.toPersianDate(date)
    }
    
    constructor(year: Int, month: Int, day: Int) {
        persianDate = PersianDate(year, month, day)
    }
    
    fun getPersianDate(): PersianDate = persianDate
    
    fun getYear(): Int = persianDate.year
    fun getMonth(): Int = persianDate.month
    fun getDay(): Int = persianDate.day
    
    fun setDate(year: Int, month: Int, day: Int) {
        persianDate = PersianDate(year, month, day)
    }
    
    fun addDays(days: Int) {
        persianDate = persianDate.addDays(days)
    }
    
    fun addMonths(months: Int) {
        persianDate = persianDate.addMonths(months)
    }
    
    fun getGregorianDate(): Date {
        return PersianDate.fromPersianDate(persianDate)
    }
    
    fun getFirstDayOfMonth(): PersianDate {
        return PersianDate(persianDate.year, persianDate.month, 1)
    }
    
    fun getDaysInCurrentMonth(): Int {
        return PersianDate.getDaysInMonth(persianDate.year, persianDate.month)
    }
    
    fun getFirstDayOfWeekInMonth(): Int {
        val firstDay = getFirstDayOfMonth()
        return firstDay.getDayOfWeek()
    }
    
    fun isToday(): Boolean {
        val today = PersianDate.toPersianDate(Date())
        return persianDate.isSameDay(today)
    }
} 