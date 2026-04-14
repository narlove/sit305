package me.narlove.calendar.custom;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleItem {
    private final String title;
    private final String category;
    private final String location;
    private final Date date;

    public SingleItem(String title, String category, String location,
                      Date date)
    {
        this.title = title;
        this.category = category;
        this.location = location;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString()
    {
        String pattern = "HH:mm:ss dd/MM/yyyy";

        return this.getDateString(pattern);
    }

    public String getDateString(String pattern)
    {
        DateFormat df = new SimpleDateFormat(pattern);

        return df.format(this.date);
    }
}
