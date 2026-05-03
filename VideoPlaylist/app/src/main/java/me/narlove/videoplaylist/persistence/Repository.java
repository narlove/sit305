package me.narlove.videoplaylist.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.narlove.videoplaylist.utilities.AuthCallback;

public class Repository {
    private final UserDao userDao;
    private final PlaylistDao playlistDao;
    private final LiveData<List<UserWithPlaylistEntries>> users;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public Repository(Application app) {
        AppDatabase db = Room.databaseBuilder(
                        app,
                        AppDatabase.class,
                        "user-and-playlist-database")
                // .createFromAsset("database/user-and-playlist-database.db")
                .build();
        this.userDao = db.userDao();
        this.playlistDao = db.playlistDao();

        this.users = this.userDao.getAll();
    }

    public LiveData<List<UserWithPlaylistEntries>> getAllUsers()
    {
        return users;
    }

    public LiveData<UserWithPlaylistEntries> getUser(long uid)
    {
        return userDao.getUser(uid);
    }

    public UserWithPlaylistEntries getUserByUsername(String name)
    {
        return userDao.getUserByUsername(name);
    }

    public void authenticate(String username, String password, AuthCallback callback)
    {
        executor.execute(() -> {
            UserWithPlaylistEntries userWithEntries = userDao.getUserByUsername(username);

            if (userWithEntries != null &&
                    userWithEntries.user.getPassword().equals(password))
            {
                callback.onSuccess(userWithEntries);
            }
            else
            {
                callback.onFailure();
            }
        });
    }

    public void insertUser(User user)
    {
        executor.execute(() -> this.userDao.insert(user));
    }

    public void updateUser(User user)
    {
        executor.execute(() -> this.userDao.update(user));
    }

    public void deleteUser(User user)
    {
        executor.execute(() -> this.userDao.delete(user));
    }

    public void insertPlaylistEntry(PlaylistEntry entry)
    {
        executor.execute(() -> playlistDao.insert(entry));
    }

    public int usersSize()
    {
        return this.userDao.size();
    }
}
