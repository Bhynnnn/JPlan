package com.example.jplan.Model;

public class mCalendar {
    String year;
    String date;
    String day;
    String month;

    public mCalendar(String year, String month, String date, String day) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
