package me.narlove.enhancedlearningapp.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.narlove.enhancedlearningapp.Interest;
import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;
import me.narlove.enhancedlearningapp.persistence.datatypes.User;
import me.narlove.enhancedlearningapp.callbacks.IdentifyQuestionCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserIdCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUsernameExistsCallback;
import me.narlove.enhancedlearningapp.persistence.datatypes.IRepository;
import me.narlove.enhancedlearningapp.callbacks.IdentifyTaskAndQuestionsCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserCallback;

public class DatabaseViewModel extends AndroidViewModel {
    private final IRepository repo;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);

        // swap this and we can change in and out of mongo and room very easily
        // ensure you're changing datatypes from original version to Mongo version
        this.repo = new MongoRepo();
    }

    public void getUserByUsername(String username, IdentifyUserCallback callback)
    {
        this.repo.getUserByUsername(username, callback);
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

    public void getTaskAndQuestionsByTaskId(long taskId,
                                            IdentifyTaskAndQuestionsCallback callback)
    {
        this.repo.getTaskAndQuestionsByTaskId(taskId, callback);
    }

    public void deleteTaskById(long taskId)
    {
        this.repo.deleteTaskById(taskId);
    }

    public void insertTaskWithQuestions(Task task, List<Question> questions)
    {
        this.repo.insertTaskWithQuestions(task, questions);
    }

    public LiveData<Integer> getNumberOfTasksByUserId(long uid)
    {
        return this.repo.getNumberOfTasksByUserId(uid);
    }

    public void getQuestionByQuestionId(long questionId,
                                        IdentifyQuestionCallback callback)
    {
        this.repo.getQuestionByQuestionId(questionId, callback);
    }
}
