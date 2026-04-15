package me.narlove.calendar.helpers;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.narlove.calendar.custom.SingleItem;
import me.narlove.calendar.persistence.Repository;

public class EventsViewModel extends ViewModel {
    // now we're storing the data here so that it can be accessed between fragments
    private final LiveData<List<SingleItem>> allItems;
    private Repository repo;

    public EventsViewModel(Context context) {
        repo = new Repository(context);
        allItems = repo.getAllItems();
    }

    public void addItem(SingleItem item) {
        repo.insert(item);
    }

    public void replaceItem(SingleItem item) {
        repo.update(item);
    }

    public SingleItem getItemById(long id) {
        return repo.getById(id);
    }

    public long getItemIdByPosition(int position)
    {
        return getItemAtPosition(position).getId();
    }

    public SingleItem getItemAtPosition(int position)
    {
        return allItems.getValue().get(position);
    }

    public int getCurrentDatasetLength()
    {
        return repo.size();
    }

    public LiveData<List<SingleItem>> getItems()
    {
        return allItems;
    }

    public void removeItemById(long id)
    {
        repo.delete(getItemById(id));
    }
}
