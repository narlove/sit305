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
public interface UserDao {
    // https://developer.android.com/training/data-storage/room/relationships/one-to-many#java
    // do i just have to have this one or am i allowed SELECT * FROM users; without playlist entries?
    @Transaction
    @Query("SELECT * from users")
    LiveData<List<UserWithPlaylistEntries>> getAll();

    @Transaction
    @Query("SELECT * from users WHERE userId LIKE :uid")
    LiveData<UserWithPlaylistEntries> getUser(long uid);

    @Transaction
    @Query("SELECT * FROM users WHERE username LIKE :name LIMIT 1")
    UserWithPlaylistEntries getUserByUsername(String name);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    // not actually sure if this will work because this may need to be marked transaction
    // additionally, not sure if it will work logically because if it attempts to join beforehand
    // then an inner join will artifically inflate this number
    @Query("SELECT COUNT(*) FROM users")
    int size();
}
