package me.narlove.chatbot.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MessageObject.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();
}
