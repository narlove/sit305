package me.narlove.calendar.custom;

import java.io.InvalidClassException;
import java.util.Date;
import java.util.GregorianCalendar;

// it was annoying me trying to store half a date and half a time
// until i could merge them so if there is one thing
// android has taught me its that the more classes there are, the better it is
public class BrokenDateTime {
    private int hour;
    private int minute;

    private int day;
    private int month;
    private int year;

    public BrokenDateTime() {
        this.hour = -1;
        this.minute = -1;
        this.day = -1;
        this.month = -1;
        this.year = -1;
    }

    public Date getMergedDateObject() throws InvalidClassException {
        if (!allFieldsValid()) throw new InvalidClassException("not all values have been assigned");

        return new GregorianCalendar(this.year, this.month, this.day, this.hour, this.minute)
                .getTime();
    }

    public boolean allFieldsValid()
    {
        return this.hour != -1 && this.minute != -1 && this.day != -1
                && this.month != -1 && this.year != -1;
    }

    // infinite getters and setters
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
