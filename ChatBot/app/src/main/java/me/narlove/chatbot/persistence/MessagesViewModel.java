package me.narlove.chatbot.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.narlove.chatbot.utilities.ScrollCallback;

public class MessagesViewModel extends AndroidViewModel {

    private final Repository repo;
    private final LiveData<List<MessageObject>> items;

    public MessagesViewModel(@NonNull Application application) {
        super(application);

        repo = new Repository(application);
        items = repo.getAllItems();
    }

    public LiveData<List<MessageObject>> getAllItems()
    {
        return items;
    }

    public MessageObject getObject(long uid)
    {
        return repo.getObject(uid);
    }

    public void insert(MessageObject item)
    {
        repo.insert(item);
    }

    public int size()
    {
        return repo.size();
    }
}
