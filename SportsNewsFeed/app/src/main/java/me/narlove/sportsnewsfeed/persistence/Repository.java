package me.narlove.sportsnewsfeed.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.narlove.sportsnewsfeed.utilities.Category;

public class Repository {
    private final FeedDao dao;
    private final LiveData<List<FeedItem>> items;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public Repository(Application app) {
        AppDatabase db = Room.databaseBuilder(
                app,
                AppDatabase.class,
                "feed-items-database")
                .createFromAsset("database/feed-items-database.db")
                .build();
        this.dao = db.itemDao();

        this.items = this.dao.getAll();
    }

    public LiveData<List<FeedItem>> getAllItems()
    {
        return items;
    }

    public LiveData<List<FeedItem>> getItemByCategory(Category category)
    {
        return dao.getItemByCategory(category);
    }

    public LiveData<List<FeedItem>> getItemByCategories(List<Category> categories)
    {
        return dao.getItemByCategories(categories);
    }

    public LiveData<List<FeedItem>> getFavouriteItems()
    {
        return dao.getFavouriteItems();
    }

    public FeedItem getItem(long uid)
    {
        return dao.getItem(uid);
    }

    public void insert(FeedItem item)
    {
        executor.execute(() -> dao.insert(item));
    }

    public void update(FeedItem item)
    {
        executor.execute(() -> dao.update(item));
    }

    public void setFavouriteById(long uid, boolean val)
    {
        executor.execute(() -> dao.setFavouriteById(uid, val));
    }

    public void delete(FeedItem item)
    {
        executor.execute(() -> dao.delete(item));
    }

    public int size()
    {
        return dao.size();
    }
}
