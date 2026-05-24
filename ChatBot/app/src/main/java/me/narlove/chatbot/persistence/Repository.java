package me.narlove.chatbot.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.narlove.chatbot.utilities.ScrollCallback;

public class Repository {
    private final MessageDao dao;
    private final LiveData<List<MessageObject>> items;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public Repository(Application app) {
        AppDatabase db = Room.databaseBuilder(
                app,
                AppDatabase.class,
                "message-objects-database")
                .build();
        this.dao = db.messageDao();

        this.items = this.dao.getAll();
    }

    public LiveData<List<MessageObject>> getAllItems()
    {
        return items;
    }

    public MessageObject getObject(long uid)
    {
        return dao.getObject(uid);
    }

    public void insert(MessageObject item)
    {
        executor.execute(() -> dao.insert(item));
    }

    public int size()
    {
        return dao.size();
    }
}
