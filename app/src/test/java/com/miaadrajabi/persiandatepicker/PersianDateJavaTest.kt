package com.miaadrajabi.persiandatepicker

import com.miaadrajabi.persiandatepicker.utils.PersianDate
import com.miaadrajabi.persiandatepicker.utils_java.PersianDateImpl
import org.junit.Test
import org.junit.Assert.*
import java.util.*

/**
 * Unit test برای کلاس های Java
 * مقایسه نتایج با نسخه Kotlin
 */
class PersianDateJavaTest {

    @Test
    fun testDateConversion() {
        // Test conversion for 15 July 2024
        val calendar = Calendar.getInstance()
        calendar.set(2024, 6, 15) // 15 July 2024
        val testDate = calendar.time
        
        val kotlinDate = PersianDate.toPersianDate(testDate)
        val javaDate = PersianDateImpl.toPersianDate(testDate)
        
        // Compare results
        assertEquals("سال باید یکسان باشد", kotlinDate.year, javaDate.year)
        assertEquals("ماه باید یکسان باشد", kotlinDate.month, javaDate.month)
        assertEquals("روز باید یکسان باشد", kotlinDate.day, javaDate.day)
        assertEquals("فرمت تاریخ باید یکسان باشد", kotlinDate.getFormattedDate(), javaDate.getFormattedDate())
    }

    @Test
    fun testTodayDate() {
        // Test today's date
        val today = Date()
        
        val kotlinToday = PersianDate.toPersianDate(today)
        val javaToday = PersianDateImpl.toPersianDate(today)
        
        assertEquals("سال امروز باید یکسان باشد", kotlinToday.year, javaToday.year)
        assertEquals("ماه امروز باید یکسان باشد", kotlinToday.month, javaToday.month)
        assertEquals("روز امروز باید یکسان باشد", kotlinToday.day, javaToday.day)
    }

    @Test
    fun testPersianNumbers() {
        // Test Persian digits
        val numbers = arrayOf(0, 1, 10, 100, 1403, 2024)
        
        for (number in numbers) {
            val kotlinResult = PersianDate.toPersianNumber(number)
            val javaResult = PersianDateImpl.toPersianNumber(number)
            
            assertEquals("عدد فارسی $number باید یکسان باشد", kotlinResult, javaResult)
        }
    }

    @Test
    fun testLeapYear() {
        // Test leap year
        val years = arrayOf(1403, 1404, 1405, 1406, 1407, 1408)
        
        for (year in years) {
            val kotlinResult = PersianDate.isLeapYear(year)
            val javaResult = PersianDateImpl.isLeapYear(year)
            
            assertEquals("سال کبیسه $year باید یکسان باشد", kotlinResult, javaResult)
        }
    }

    @Test
    fun testDaysInMonth() {
        // Test days in month
        val year = 1403
        
        for (month in 1..12) {
            val kotlinResult = PersianDate.getDaysInMonth(year, month)
            val javaResult = PersianDateImpl.getDaysInMonth(year, month)
            
            assertEquals("تعداد روزهای ماه $month باید یکسان باشد", kotlinResult, javaResult)
        }
    }

    @Test
    fun testMonthNames() {
        // Test month names
        for (month in 1..12) {
            val kotlinDate = PersianDate(1403, month, 1)
            val javaDate = PersianDateImpl(1403, month, 1)
            
            assertEquals("نام ماه $month باید یکسان باشد", kotlinDate.getMonthName(), javaDate.getMonthName())
        }
    }

    @Test
    fun testDateComparison() {
        // Test date comparisons
        val date1 = PersianDateImpl(1403, 4, 24)
        val date2 = PersianDateImpl(1403, 4, 25)
        val date3 = PersianDateImpl(1403, 4, 24)
        
        assertTrue("date1 باید قبل از date2 باشد", date1.isBefore(date2))
        assertFalse("date1 نباید بعد از date2 باشد", date1.isAfter(date2))
        assertTrue("date1 باید مساوی date3 باشد", date1.isSameDay(date3))
    }

    @Test
    fun testFormattedDate() {
        // Test date formatting
        val testDate = PersianDateImpl(1403, 4, 24)
        
        val formatted = testDate.getFormattedDate()
        val numeric = testDate.getNumericFormattedDate()
        
        assertTrue("فرمت تاریخ باید شامل اعداد فارسی باشد", formatted.contains("۲۴"))
        assertTrue("فرمت تاریخ باید شامل نام ماه باشد", formatted.contains("تیر"))
        assertTrue("فرمت تاریخ باید شامل سال باشد", formatted.contains("۱۴۰۳"))
        
        assertEquals("فرمت عددی باید درست باشد", "۱۴۰۳/۰۴/۲۴", numeric)
    }

    @Test
    fun testStringConversion() {
        // Test conversion of Persian strings
        val testStrings = arrayOf("123", "2024", "1403/04/24")
        
        for (str in testStrings) {
            val kotlinResult = PersianDate.toPersianNumber(str)
            val javaResult = PersianDateImpl.toPersianNumber(str)
            
            assertEquals("تبدیل رشته $str باید یکسان باشد", kotlinResult, javaResult)
        }
    }

    @Test
    fun testEdgeCases() {
        // Edge cases tests
        
        // Test Nowruz (start of year)
        val newYear = Calendar.getInstance()
        newYear.set(2024, 2, 20) // 20 March 2024 (Nowruz)
        
        val kotlinNewYear = PersianDate.toPersianDate(newYear.time)
        val javaNewYear = PersianDateImpl.toPersianDate(newYear.time)
        
        assertEquals("سال نو باید یکسان باشد", kotlinNewYear.getFormattedDate(), javaNewYear.getFormattedDate())
        
        // Test end of Esfand
        val endOfYear = Calendar.getInstance()
        endOfYear.set(2024, 2, 19) // 19 March 2024 (end of Esfand)
        
        val kotlinEndYear = PersianDate.toPersianDate(endOfYear.time)
        val javaEndYear = PersianDateImpl.toPersianDate(endOfYear.time)
        
        assertEquals("آخر سال باید یکسان باشد", kotlinEndYear.getFormattedDate(), javaEndYear.getFormattedDate())
    }
} 