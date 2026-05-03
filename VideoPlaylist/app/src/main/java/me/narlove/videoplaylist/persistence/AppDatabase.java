package me.narlove.videoplaylist.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, PlaylistEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PlaylistDao playlistDao();
}
