package com.miaadrajabi.persiandatepicker.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Persian Calendar Date Utility Class
 */
public class PersianDate {
    private final int year;
    private final int month; // 1-12
    private final int day;

    public static final String[] PERSIAN_MONTHS = {
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    };

    public static final String[] PERSIAN_WEEKDAYS = {
        "شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه"
    };

    public static final String[] PERSIAN_WEEKDAYS_SHORT = {
        "ش", "ی", "د", "س", "چ", "پ", "ج"
    };

    public PersianDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

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

    public static Date fromPersianDate(PersianDate persianDate) {
        Calendar calendar = Calendar.getInstance();
        int[] gregorianDate = jalaliToGregorian(persianDate.year, persianDate.month, persianDate.day);
        calendar.set(gregorianDate[0], gregorianDate[1] - 1, gregorianDate[2]);
        return calendar.getTime();
    }

    public static PersianDate toPersianDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int[] jalaliDate = gregorianToJalali(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        return new PersianDate(jalaliDate[0], jalaliDate[1], jalaliDate[2]);
    }

    public static boolean isLeapYear(int year) {
        int cycle = year % 33;
        int[] leapYears = {1, 5, 9, 13, 17, 22, 26, 30};
        for (int leapYear : leapYears) {
            if (cycle == leapYear) {
                return true;
            }
        }
        return false;
    }

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

    private static int[] jalaliToGregorian(int jy, int jm, int jd) {
        int gy = jy + 621;
        int daysFromJalaliNewYear;
        if (jm <= 6) {
            daysFromJalaliNewYear = (jm - 1) * 31 + jd;
        } else {
            daysFromJalaliNewYear = 186 + (jm - 7) * 30 + jd;
        }
        int march21 = 80;

        int dayOfYear = march21 + daysFromJalaliNewYear - 1;

        boolean isLeap = ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0));
        int daysInYear = isLeap ? 366 : 365;

        int[] monthDays = isLeap ?
            new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31} :
            new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (dayOfYear <= daysInYear) {
            int gm = 1;
            int remainingDays = dayOfYear;

            for (int i = 0; i < 12; i++) {
                if (remainingDays <= monthDays[i]) {
                    gm = i + 1;
                    break;
                }
                remainingDays -= monthDays[i];
            }

            return new int[]{gy, gm, remainingDays};
        } else {
            int nextYear = gy + 1;
            boolean isNextLeap = ((nextYear % 4 == 0 && nextYear % 100 != 0) || (nextYear % 400 == 0));
            int[] nextMonthDays = isNextLeap ?
                new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31} :
                new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            int gm = 1;
            int remainingDays = dayOfYear - daysInYear;

            for (int i = 0; i < 12; i++) {
                if (remainingDays <= nextMonthDays[i]) {
                    gm = i + 1;
                    break;
                }
                remainingDays -= nextMonthDays[i];
            }

            return new int[]{nextYear, gm, remainingDays};
        }
    }

    private static int[] gregorianToJalali(int gy, int gm, int gd) {
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean isLeap = ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0));
        if (isLeap) monthDays[1] = 29;

        int dayOfYear = gd;
        for (int i = 0; i < gm - 1; i++) {
            dayOfYear += monthDays[i];
        }

        int jy = gy - 621;
        int march21 = 80;
        if (dayOfYear < march21) {
            int prevYear = jy - 1;
            int daysFromPrevNewYear = dayOfYear + (isLeap ? 366 : 365) - march21;
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

    public String getMonthName() {
        return PERSIAN_MONTHS[month - 1];
    }

    public String getFormattedDate() {
        String dayStr = toPersianNumber(day);
        String monthStr = getMonthName();
        String yearStr = toPersianNumber(year);
        return dayStr + " " + monthStr + " " + yearStr;
    }

    public String getShortFormattedDate() {
        return toPersianNumber(year) + "/" +
               toPersianNumber(String.format("%02d", month)) + "/" +
               toPersianNumber(String.format("%02d", day));
    }

    public String getNumericFormattedDate() {
        return toPersianNumber(year) + "/" +
               toPersianNumber(String.format("%02d", month)) + "/" +
               toPersianNumber(String.format("%02d", day));
    }

    public String getFullFormattedDate() {
        return getFormattedDate() + "\n" + getNumericFormattedDate();
    }

    public PersianDate addDays(int days) {
        Date gregorianDate = fromPersianDate(this);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gregorianDate);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return toPersianDate(calendar.getTime());
    }

    public PersianDate addMonths(int months) {
        int newYear = year;
        int newMonth = month + months;

        while (newMonth > 12) {
            newMonth -= 12;
            newYear += 1;
        }

        while (newMonth < 1) {
            newMonth += 12;
            newYear -= 1;
        }

        int maxDay = getDaysInMonth(newYear, newMonth);
        int newDay = (day > maxDay) ? maxDay : day;

        return new PersianDate(newYear, newMonth, newDay);
    }

    public boolean isSameDay(PersianDate other) {
        return year == other.year && month == other.month && day == other.day;
    }

    public boolean isAfter(PersianDate other) {
        if (year > other.year) return true;
        if (year < other.year) return false;
        if (month > other.month) return true;
        if (month < other.month) return false;
        return day > other.day;
    }

    public boolean isBefore(PersianDate other) {
        if (year < other.year) return true;
        if (year > other.year) return false;
        if (month < other.month) return true;
        if (month > other.month) return false;
        return day < other.day;
    }

    public int getDayOfWeek() {
        Date gregorianDate = fromPersianDate(this);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gregorianDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        dayOfWeek = (dayOfWeek + 1) % 7;

        return dayOfWeek;
    }

    public String getDayOfWeekName() {
        return PERSIAN_WEEKDAYS[getDayOfWeek()];
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
}
