package me.narlove.lostandfoundjava.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import me.narlove.lostandfoundjava.utilities.PostCategory;
import me.narlove.lostandfoundjava.utilities.PostType;

@Dao
public interface PostDao {
    @Query("SELECT * FROM posts")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * FROM posts WHERE postCategory LIKE :category")
    LiveData<List<Post>> getPostsByCategory(PostCategory category);

    @Query("SELECT * FROM posts WHERE postCategory IN (:category) AND postType IN (:type)")
    LiveData<List<Post>> getPostsByCategoryOfType(List<PostCategory> category, List<PostType> type);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Post post);

    @Delete
    void delete(Post post);
}
