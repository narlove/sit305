package me.narlove.sportsnewsfeed.persistence;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;

@Database(entities = {FeedItem.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FeedDao itemDao();
}
