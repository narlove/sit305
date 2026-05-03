package me.narlove.videoplaylist.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.narlove.videoplaylist.utilities.AuthCallback;

public class DatabaseViewModel extends AndroidViewModel {

    private final Repository repo;
    // shortened the name down from allUsersWithPlaylistEntries but if it becomes too hard to read
    // or to comprehend i will change it back
    private final LiveData<List<UserWithPlaylistEntries>> users;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);

        repo = new Repository(application);
        users = repo.getAllUsers();
    }

    public LiveData<List<UserWithPlaylistEntries>> getAllUsers()
    {
        return users;
    }

    public LiveData<UserWithPlaylistEntries> getUser(long uid)
    {
        return repo.getUser(uid);
    }

    public UserWithPlaylistEntries getUserByUsername(String name)
    {
        return repo.getUserByUsername(name);
    }

    public void authenticate(String username, String password, AuthCallback callback)
    {
        repo.authenticate(username, password, callback);
    }

    public void insertUser(User user)
    {
        repo.insertUser(user);
    }

    public void insertPlaylistEntry(PlaylistEntry entry)
    {
        repo.insertPlaylistEntry(entry);
    }

    public void updateUser(User user)
    {
        repo.updateUser(user);
    }

    public void deleteUser(User user)
    {
        repo.deleteUser(user);
    }

    public int usersSize()
    {
        return repo.usersSize();
    }
}
