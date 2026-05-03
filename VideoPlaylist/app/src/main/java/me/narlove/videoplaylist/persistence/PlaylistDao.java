package me.narlove.videoplaylist.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Query("SELECT * from playlistEntries")
    LiveData<List<PlaylistEntry>> getAll();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(PlaylistEntry entry);

    @Update
    void update(PlaylistEntry entry);

    @Delete
    void delete(PlaylistEntry entry);

    @Query("SELECT COUNT(*) FROM playlistEntries")
    int size();
}
