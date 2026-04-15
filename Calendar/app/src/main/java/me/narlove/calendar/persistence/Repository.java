package me.narlove.calendar.persistence;

import static androidx.room.Room.databaseBuilder;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.narlove.calendar.custom.SingleItem;

public class Repository {
    private ItemDao dao;
    private final AppDatabase db;

    private final LiveData<List<SingleItem>> allItems;

    public Repository(Context context) {
        db = databaseBuilder(context, AppDatabase.class, "events-database").allowMainThreadQueries().build();
        dao = db.eventDao();

        allItems = dao.getAll();
    }

    public LiveData<List<SingleItem>> getAllItems() {
        return allItems;
    }

    public void update(SingleItem item)
    {
        dao.update(item);
    }

    public void insert(SingleItem item) {
        dao.insert(item);
    }

    public void delete(SingleItem item)
    {
        dao.delete(item);
    }

    public SingleItem getById(long id)
    {
        return dao.getItem(id);
    }

    public int size()
    {
        return dao.size();
    }
}
