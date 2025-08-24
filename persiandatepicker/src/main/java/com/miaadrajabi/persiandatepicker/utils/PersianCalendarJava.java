package com.miaadrajabi.persiandatepicker.utils;

import java.util.Date;


public class PersianCalendarJava {
    private PersianDate persianDate;
    
    public PersianCalendarJava() {
        this.persianDate = PersianDate.toPersianDate(new Date());
    }
    
    public PersianCalendarJava(Date date) {
        this.persianDate = PersianDate.toPersianDate(date);
    }
    
    public PersianCalendarJava(int year, int month, int day) {
        this.persianDate = new PersianDate(year, month, day);
    }
    
    public PersianDate getPersianDate() { 
        return persianDate; 
    }
    
    public int getYear() { 
        return persianDate.getYear(); 
    }
    
    public int getMonth() { 
        return persianDate.getMonth(); 
    }
    
    public int getDay() { 
        return persianDate.getDay(); 
    }
    
    public void setDate(int year, int month, int day) {
        this.persianDate = new PersianDate(year, month, day);
    }
    
    public void addDays(int days) {
        this.persianDate = persianDate.addDays(days);
    }
    
    public void addMonths(int months) {
        this.persianDate = persianDate.addMonths(months);
    }
    
    public Date getGregorianDate() {
        return PersianDate.fromPersianDate(persianDate);
    }
    
    public PersianDate getFirstDayOfMonth() {
        return new PersianDate(persianDate.getYear(), persianDate.getMonth(), 1);
    }
    
    public int getDaysInCurrentMonth() {
        return PersianDate.getDaysInMonth(persianDate.getYear(), persianDate.getMonth());
    }
    
    public int getFirstDayOfWeekInMonth() {
        PersianDate firstDay = getFirstDayOfMonth();
        return firstDay.getDayOfWeek();
    }
    
    public boolean isToday() {
        PersianDate today = PersianDate.toPersianDate(new Date());
        return persianDate.isSameDay(today);
    }
} 