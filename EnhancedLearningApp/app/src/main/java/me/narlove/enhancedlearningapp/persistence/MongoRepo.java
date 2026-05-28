package me.narlove.enhancedlearningapp.persistence;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.narlove.enhancedlearningapp.Interest;
import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;
import me.narlove.enhancedlearningapp.persistence.datatypes.User;
import me.narlove.enhancedlearningapp.callbacks.IdentifyQuestionCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyTaskAndQuestionsCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserIdCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUsernameExistsCallback;
import me.narlove.enhancedlearningapp.persistence.datatypes.IRepository;

public class MongoRepo implements IRepository {

    public MongoRepo()
    {

    }

    @Override
    public void getUserByUsername(String username, IdentifyUserCallback callback) {

    }

    @Override
    public void insert(User user, IdentifyUserIdCallback callback) {

    }

    @Override
    public void overrideInterests(long userId, List<Interest> newInterests) {

    }

    @Override
    public void doesUsernameExist(String username, IdentifyUsernameExistsCallback callback) {

    }

    @Override
    public LiveData<List<Task>> getTasksByUserId(long uid) {
        return null;
    }

    @Override
    public void getTaskAndQuestionsByTaskId(long taskId, IdentifyTaskAndQuestionsCallback callback) {

    }

    @Override
    public void deleteTaskById(long taskId) {

    }

    @Override
    public void insertTaskWithQuestions(Task task, List<Question> questions) {

    }

    @Override
    public LiveData<Integer> getNumberOfTasksByUserId(long uid) {
        return null;
    }

    @Override
    public void getQuestionByQuestionId(long questionId, IdentifyQuestionCallback callback) {

    }
}
