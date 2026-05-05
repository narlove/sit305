package me.narlove.lostandfoundjava.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.narlove.lostandfoundjava.utilities.PostCategory;
import me.narlove.lostandfoundjava.utilities.PostType;

public class Repository {
    private final PostDao postDao;
    private final LiveData<List<Post>> posts;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public Repository(Application app) {
        AppDatabase db = Room.databaseBuilder(
                        app,
                        AppDatabase.class,
                        "posts-database")
                // .createFromAsset("database/posts-database.db")
                .build();
        this.postDao = db.postDao();

        this.posts = this.postDao.getAllPosts();
    }

    public LiveData<List<Post>> getAllPosts()
    {
        return this.posts;
    }

    public LiveData<List<Post>> getPostsByCategory(PostCategory category)
    {
        return this.postDao.getPostsByCategory(category);
    }

    public LiveData<List<Post>> getPostsByCategoryOfType(List<PostCategory> category, List<PostType> type)
    {
        return this.postDao.getPostsByCategoryOfType(category, type);
    }

    public void insert(Post post)
    {
        executor.execute(() -> this.postDao.insert(post));
    }

    public void delete(Post post)
    {
        executor.execute(() -> this.postDao.delete(post));
    }
}
