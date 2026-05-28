package me.narlove.enhancedlearningapp.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.narlove.enhancedlearningapp.Interest;
import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;
import me.narlove.enhancedlearningapp.persistence.datatypes.User;
import me.narlove.enhancedlearningapp.callbacks.IdentifyQuestionCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserIdCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUsernameExistsCallback;
import me.narlove.enhancedlearningapp.persistence.daos.QuestionDao;
import me.narlove.enhancedlearningapp.persistence.daos.TaskDao;
import me.narlove.enhancedlearningapp.persistence.daos.UserDao;
import me.narlove.enhancedlearningapp.persistence.datatypes.IRepository;
import me.narlove.enhancedlearningapp.persistence.datatypes.TaskWithQuestions;
import me.narlove.enhancedlearningapp.callbacks.IdentifyTaskAndQuestionsCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserCallback;
import me.narlove.enhancedlearningapp.utilities.InterestConversionHandler;

public class RoomRepo implements IRepository {
    private final UserDao userDao;
    private final TaskDao taskDao;
    private final QuestionDao questionDao;
    private final LiveData<List<User>> users;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public RoomRepo(Application app) {
        AppDatabase db = Room.databaseBuilder(
                        app,
                        AppDatabase.class,
                        "enhanced-learning-database")
                .build();
        this.userDao = db.userDao();
        this.taskDao = db.taskDao();
        this.questionDao = db.questionDao();

        this.users = this.userDao.getAllUsers();
    }

    // from user dao
    public LiveData<List<User>> getAllUsers()
    {
        return this.users;
    }

    // might only be able to use not live data with a callback
    public User getUserById(long uid)
    {
        return this.userDao.getUserById(uid);
    }

    public void getUserByUsername(String username, IdentifyUserCallback callback)
    {
        executor.execute(() ->
        {
            User user = this.userDao.getUserByUsername(username);
            if (user != null)
            {
                callback.onSuccess(user);
            }
            else
            {
                callback.onFailure();
            }
        });
    }

    public User getUserByUsername(String username)
    {
        return this.userDao.getUserByUsername(username);
    }

    public void insert(User user)
    {
        executor.execute(() -> this.userDao.insert(user));
    }

    public void insert(User user, IdentifyUserIdCallback callback)
    {
        executor.execute(() ->
        {
            long id = this.userDao.insert(user);
            callback.onSuccess(id);
        });
    }

    public void overrideInterests(long userId, List<Interest> newInterests)
    {
        executor.execute(() -> this.userDao.overrideInterests(userId,
                // some reason typeconverter isn't working, we will force it to
                // manually
                InterestConversionHandler.interestListToString(newInterests)));
    }

    public void doesUsernameExist(String username, IdentifyUsernameExistsCallback callback)
    {
        executor.execute(() ->
        {
            boolean result = this.userDao.doesUsernameExist(username);
            if (result) callback.onUsernameExists();
            else callback.onUsernameDoesNotExist();
        });
    }

    // from taskdao
    public LiveData<List<Task>> getTasksByUserId(long uid)
    {
        return this.taskDao.getTasksByUserId(uid);
    }

    // again, not sure this will actually work because it'll attempt to contact
    // db on ui thread
    public TaskWithQuestions getTaskAndQuestionsByTaskId(long taskId)
    {
        return this.taskDao.getTaskAndQuestionsByTaskId(taskId);
    }

    // the primary method i think
    public void getTaskAndQuestionsByTaskId(long taskId,
                                            IdentifyTaskAndQuestionsCallback callback)
    {
        executor.execute(() ->
        {
            TaskWithQuestions tasks = this.taskDao.getTaskAndQuestionsByTaskId(taskId);
            if (tasks != null)
            {
                callback.onSuccess(tasks);
            }
            else
            {
                callback.onFailure();
            }
        });
    }

    public void deleteTaskById(long taskId)
    {
        executor.execute(() -> this.taskDao.deleteTaskById(taskId));
    }

    public void insertTask(Task task)
    {
        executor.execute(() -> this.taskDao.insertTask(task));
    }

    public void insertTaskWithQuestions(Task task, List<Question> questions)
    {
        executor.execute(() -> this.taskDao.insertTaskWithQuestions(task, questions));
    }

    public LiveData<Integer> getNumberOfTasksByUserId(long uid)
    {
        return this.taskDao.getNumberOfTasksByUserId(uid);
    }

    public void getQuestionByQuestionId(long questionId,
                                            IdentifyQuestionCallback callback)
    {
        executor.execute(() ->
        {
            Question q = this.questionDao.getQuestionById(questionId);
            if (q != null)
            {
                callback.onSuccess(q);
            }
            else
            {
                callback.onFailure();
            }
        });
    }
}
