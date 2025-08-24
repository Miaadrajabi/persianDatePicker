package com.miaadrajabi.persiandatepicker.utils_java;

import java.util.Calendar;
import java.util.Date;

/**
 * Persian Date class in Java
 * Java implementation of Persian date class
 */
public class PersianDateImpl {
    private final int year;
    private final int month; // 1-12
    private final int day;

    // Persian month names
    public static final String[] PERSIAN_MONTHS = {
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    };

    // Persian weekdays
    public static final String[] PERSIAN_WEEKDAYS = {
        "شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه"
    };

    // Abbreviated weekdays
    public static final String[] PERSIAN_WEEKDAYS_SHORT = {
        "ش", "ی", "د", "س", "چ", "پ", "ج"
    };

    public PersianDateImpl(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    // Convert English digits to Persian digits
    public static String toPersianNumber(String number) {
        String[] persianDigits = {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
        String result = number;
        for (int i = 0; i <= 9; i++) {
            result = result.replace(String.valueOf(i), persianDigits[i]);
        }
        return result;
    }

    public static String toPersianNumber(int number) {
        return toPersianNumber(String.valueOf(number));
    }

    // Convert Gregorian date to Jalali (Persian) date
    public static PersianDateImpl toPersianDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int[] jalaliDate = gregorianToJalali(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        return new PersianDateImpl(jalaliDate[0], jalaliDate[1], jalaliDate[2]);
    }

    // Check Jalali leap year
    public static boolean isLeapYear(int year) {
        // 33-year cycle of Jalali calendar
        int cycle = year % 33;
        
        // Leap years in the 33-year cycle
        int[] leapYears = {1, 5, 9, 13, 17, 22, 26, 30};
        
        for (int leapYear : leapYears) {
            if (cycle == leapYear) {
                return true;
            }
        }
        return false;
    }

    // Number of days in each Jalali month
    public static int getDaysInMonth(int year, int month) {
        if (month >= 1 && month <= 6) {
            return 31;
        } else if (month >= 7 && month <= 11) {
            return 30;
        } else if (month == 12) {
            return isLeapYear(year) ? 30 : 29;
        }
        return 0;
    }

    // Convert Gregorian to Jalali - precise algorithm
    private static int[] gregorianToJalali(int gy, int gm, int gd) {
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean isLeap = ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0));
        if (isLeap) monthDays[1] = 29;
        
        int dayOfYear = gd;
        for (int i = 0; i < gm - 1; i++) {
            dayOfYear += monthDays[i];
        }
        
        // Calculate Jalali year
        int jy = gy - 621;
        
        // Calculate Nowruz (21 March)
        int march21 = 80; // day 80 of the year (21 March)
        
        // If before Nowruz
        if (dayOfYear < march21) {
            int prevYear = jy - 1;
            int daysFromPrevNewYear = dayOfYear + (isLeap ? 366 : 365) - march21;
            
            // Calculate month and day
            if (daysFromPrevNewYear < 186) {
                int jm = daysFromPrevNewYear / 31 + 1;
                int jd = daysFromPrevNewYear % 31 + 1;
                return new int[]{prevYear, jm, jd};
            } else {
                int daysAfterSixMonths = daysFromPrevNewYear - 186;
                int jm = daysAfterSixMonths / 30 + 7;
                int jd = daysAfterSixMonths % 30 + 1;
                return new int[]{prevYear, jm, jd};
            }
        } else {
            // After Nowruz
            int daysFromNewYear = dayOfYear - march21;
            
            if (daysFromNewYear < 186) {
                int jm = daysFromNewYear / 31 + 1;
                int jd = daysFromNewYear % 31 + 1;
                return new int[]{jy, jm, jd};
            } else {
                int daysAfterSixMonths = daysFromNewYear - 186;
                int jm = daysAfterSixMonths / 30 + 7;
                int jd = daysAfterSixMonths % 30 + 1;
                return new int[]{jy, jm, jd};
            }
        }
    }

    // Utility methods for Jalali date
    public String getMonthName() {
        return PERSIAN_MONTHS[month - 1];
    }

    public String getFormattedDate() {
        String dayStr = toPersianNumber(day);
        String monthStr = getMonthName();
        String yearStr = toPersianNumber(year);
        return dayStr + " " + monthStr + " " + yearStr;
    }

    public String getNumericFormattedDate() {
        return toPersianNumber(year) + "/" + 
               toPersianNumber(String.format("%02d", month)) + "/" + 
               toPersianNumber(String.format("%02d", day));
    }

    public String getFullFormattedDate() {
        return getFormattedDate() + "\n" + getNumericFormattedDate();
    }

    public boolean isSameDay(PersianDateImpl other) {
        return year == other.year && month == other.month && day == other.day;
    }

    public boolean isAfter(PersianDateImpl other) {
        if (year > other.year) return true;
        if (year < other.year) return false;
        if (month > other.month) return true;
        if (month < other.month) return false;
        return day > other.day;
    }

    public boolean isBefore(PersianDateImpl other) {
        if (year < other.year) return true;
        if (year > other.year) return false;
        if (month < other.month) return true;
        if (month > other.month) return false;
        return day < other.day;
    }

    // Getters
    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    
    @Override
    public String toString() {
        return getFormattedDate();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PersianDateImpl other = (PersianDateImpl) obj;
        return year == other.year && month == other.month && day == other.day;
    }
} 