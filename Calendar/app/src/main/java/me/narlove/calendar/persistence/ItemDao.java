package me.narlove.calendar.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.narlove.calendar.custom.SingleItem;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM items ORDER BY date ASC")
    LiveData<List<SingleItem>> getAll();

    @Query("SELECT * FROM items WHERE id LIKE :uid")
    SingleItem getItem(long uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SingleItem item);

    @Update
    void update(SingleItem item);

    @Delete
    void delete(SingleItem item);

    @Query("SELECT COUNT(*) FROM items")
    int size();

}