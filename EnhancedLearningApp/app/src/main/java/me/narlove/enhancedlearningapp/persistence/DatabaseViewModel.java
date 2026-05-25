package me.narlove.enhancedlearningapp.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.narlove.enhancedlearningapp.datatypes.Interest;
import me.narlove.enhancedlearningapp.datatypes.Task;
import me.narlove.enhancedlearningapp.datatypes.User;
import me.narlove.enhancedlearningapp.datatypes.callbacks.IdentifyUserIdCallback;
import me.narlove.enhancedlearningapp.datatypes.callbacks.IdentifyUsernameExistsCallback;
import me.narlove.enhancedlearningapp.persistence.datatypes.TaskWithQuestions;
import me.narlove.enhancedlearningapp.datatypes.callbacks.IdentifyTaskAndQuestionsCallback;
import me.narlove.enhancedlearningapp.datatypes.callbacks.IdentifyUserCallback;

public class DatabaseViewModel extends AndroidViewModel {
    private final Repository repo;
    private final LiveData<List<User>> users;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);

        this.repo = new Repository(application);
        this.users = this.repo.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers()
    {
        return this.users;
    }

    public User getUserById(long uid)
    {
        return this.repo.getUserById(uid);
    }

    public void getUserByUsername(String username, IdentifyUserCallback callback)
    {
        this.repo.getUserByUsername(username, callback);
    }

    public User getUserByUsername(String username)
    {
        return this.repo.getUserByUsername(username);
    }

    public void insert(User user)
    {
        this.repo.insert(user);
    }

    public void insert(User user, IdentifyUserIdCallback callback)
    {
        this.repo.insert(user, callback);
    }

    public void overrideInterests(long userId, List<Interest> newInterests)
    {
        this.repo.overrideInterests(userId, newInterests);
    }

    public void doesUsernameExist(String username, IdentifyUsernameExistsCallback callback)
    {
        this.repo.doesUsernameExist(username, callback);
    }

    public LiveData<List<Task>> getTasksByUserId(long uid)
    {
        return this.repo.getTasksByUserId(uid);
    }

    public TaskWithQuestions getTaskAndQuestionsByTaskId(long taskId)
    {
        return this.repo.getTaskAndQuestionsByTaskId(taskId);
    }

    public void getTaskAndQuestionsByTaskId(long taskId,
                                            IdentifyTaskAndQuestionsCallback callback)
    {
        this.repo.getTaskAndQuestionsByTaskId(taskId, callback);
    }

    public void deleteTaskById(long taskId)
    {
        this.repo.deleteTaskById(taskId);
    }

    public void insertTask(Task task)
    {
        this.repo.insertTask(task);
    }

    public LiveData<Integer> getNumberOfTasksByUserId(long uid)
    {
        return this.repo.getNumberOfTasksByUserId(uid);
    }
}
