package me.narlove.sportsnewsfeed.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.narlove.sportsnewsfeed.utilities.Category;

public class FeedItemsViewModel extends AndroidViewModel {

    private final Repository repo;
    private final LiveData<List<FeedItem>> items;

    public FeedItemsViewModel(@NonNull Application application) {
        super(application);

        repo = new Repository(application);
        items = repo.getAllItems();
    }

    public LiveData<List<FeedItem>> getAllItems()
    {
        return items;
    }

    public LiveData<List<FeedItem>> getFavouriteItems()
    {
        return repo.getFavouriteItems();
    }

    public LiveData<List<FeedItem>> getItemByCategory(Category category)
    {
        return repo.getItemByCategory(category);
    }

    public LiveData<List<FeedItem>> getItemByCategories(List<Category> categories)
    {
        return repo.getItemByCategories(categories);
    }

    public FeedItem getItem(long uid)
    {
        return repo.getItem(uid);
    }

    public void insert(FeedItem item)
    {
        repo.insert(item);
    }

    public void update(FeedItem item)
    {
        repo.update(item);
    }

    public void setFavouriteById(long uid, boolean val)
    {
        repo.setFavouriteById(uid, val);
    }

    public void delete(FeedItem item)
    {
        repo.delete(item);
    }

    public int size()
    {
        return repo.size();
    }
}
