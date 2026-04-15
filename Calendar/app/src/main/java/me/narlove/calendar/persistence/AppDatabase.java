package me.narlove.calendar.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import me.narlove.calendar.custom.DateConverter;
import me.narlove.calendar.custom.SingleItem;

@Database(entities = {SingleItem.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDao eventDao();
}
