package me.narlove.chatbot.persistence;

import androidx.room.TypeConverter;
import java.util.Date;
import me.narlove.chatbot.utilities.Author;

public class CustomTypeConverter
{
    @TypeConverter
    public String authorToString(Author author)
    {
        return author == null ? null : author.toString();
    }

    @TypeConverter
    public Author stringToAuthor(String string)
    {
        return string == null ? null : Author.valueOf(string);
    }

    @TypeConverter
    public long dateToLong(Date date)
    {
        if (date == null) throw new IllegalArgumentException("date cannot be null");
        return date.getTime();
    }

    @TypeConverter
    public Date longToDate(long time)
    {
        return new Date(time);
    }
}
