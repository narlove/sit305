package me.narlove.sportsnewsfeed.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.narlove.sportsnewsfeed.utilities.Category;

@Dao
public interface FeedDao {
    @Query("SELECT * FROM items")
    LiveData<List<FeedItem>> getAll();

    @Query("SELECT * FROM items WHERE category LIKE :category")
    LiveData<List<FeedItem>> getItemByCategory(Category category);

    @Query("SELECT * FROM items WHERE category IN (:categories)")
    LiveData<List<FeedItem>> getItemByCategories(List<Category> categories);

    @Query("SELECT * FROM items WHERE isFavourite = 1")
    LiveData<List<FeedItem>> getFavouriteItems();

    @Query("SELECT * FROM items WHERE id LIKE :uid")
    FeedItem getItem(long uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FeedItem item);

    @Update
    void update(FeedItem item);

    @Query("UPDATE items SET isFavourite = :val WHERE id LIKE :uid")
    void setFavouriteById(long uid, boolean val);

    @Delete
    void delete(FeedItem item);

    @Query("SELECT COUNT(*) FROM items")
    int size();
}
