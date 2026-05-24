package me.narlove.chatbot.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    LiveData<List<MessageObject>> getAll();

    @Query("SELECT * FROM messages WHERE messageId LIKE (:uid)")
    MessageObject getObject(long uid);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(MessageObject item);

    @Query("SELECT COUNT(*) FROM messages")
    int size();
}
