package me.narlove.calendar.custom;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "items")
public class SingleItem {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String category;
    private String location;
    private Date date;

    public SingleItem(String title, String category, String location,
                      Date date)
    {
        this.title = title;
        this.category = category;
        this.location = location;
        this.date = date;
    }

    @Ignore
    public SingleItem(long id, String title, String category, String location,
                      Date date)
    {
        this.id = id;
        this.title = title;
        this.category = category;
        this.location = location;
        this.date = date;
    }

    public long getId()
    {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
